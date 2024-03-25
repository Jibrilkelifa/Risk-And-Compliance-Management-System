package com.cbo.CBO_NFOS_ICMS.controllers.IFRController;

import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.Images;
import com.cbo.CBO_NFOS_ICMS.services.IFRService.IncidentOrFraudService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/incidentFraudReport")
public class IncidentOrFraudController {
    private final IncidentOrFraudService incidentOrFraudService;

    public IncidentOrFraudController(IncidentOrFraudService incidentOrFraudService) {
        this.incidentOrFraudService = incidentOrFraudService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'ICMS_PROVISION')")
    public ResponseEntity<List<IncidentOrFraud>> getAllIncidentFraudReport() {
        List<IncidentOrFraud> IncidentOrFraud = incidentOrFraudService.findAllIncidentFraudReport();
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC')")
    public ResponseEntity<String> uploadForWrittenOff(@RequestParam("writtenOff") MultipartFile file) {
        try {
            incidentOrFraudService.processIncidentFraudReport(file);

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }

    @GetMapping("/getSize")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_DIRECTOR')")
    public int getIncidentFraudReportSize() {
        return incidentOrFraudService.findIncidentFraudReportSize();
    }


    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC', 'ICMS_PROVISION')")
    public ResponseEntity<List<IncidentOrFraud>> getAllIncidentFraudReportInSpecificOrganizationalUnit(@PathVariable("id") String branchId) {
        List<IncidentOrFraud> IncidentOrFraud;
        IncidentOrFraud = incidentOrFraudService.findAllIncidentFraudReportInSpecificOrganizationalUnit(branchId);
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }

    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR')")
    public ResponseEntity<List<IncidentOrFraud>> getAllIncidentFraudReportInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<IncidentOrFraud> IncidentOrFraud;
        IncidentOrFraud = incidentOrFraudService.findAllIncidentFraudReportInSpecificSubProcess(subProcessId);
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<IncidentOrFraud> getIncidentFraudReportId
            (@PathVariable("id") Long id) {
        IncidentOrFraud IncidentOrFraud = incidentOrFraudService.findIncidentFraudReportById(id);
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }




    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC','ICMS_DISTRICT_IC','ICMS_ADMIN','ICMS_DISTRICT_DIRECTOR')")
    public ResponseEntity<IncidentOrFraud> addIncidentFraudReport(
            @RequestPart("incidentOrFraud") String incidentOrFraudJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication) {

        ObjectMapper objectMapper = new ObjectMapper();
        IncidentOrFraud incidentOrFraud;

        try {
            incidentOrFraud = objectMapper.readValue(incidentOrFraudJson, IncidentOrFraud.class);

            // Set the addedByRole property based on the role of the user
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            incidentOrFraud.setAddedByRole(role);
            System.out.println("Added by role: " + role);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            if (file != null && !file.isEmpty()) {
                // Check if the file is not empty

                // Generate a unique file name
                String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());

                // Specify the file storage directory
                String filePath = "uploads/" + fileName;
                Path path = Paths.get(filePath);

                // Save the file to the specified path
                Files.write(path, file.getBytes());

                incidentOrFraud.setFileName(fileName);
                incidentOrFraud.setFilePath(filePath);

            }

            // Check if the caseId already exists
            String caseId = incidentOrFraud.getCaseId();
            while (incidentOrFraudService.isCaseIdExists(caseId)) {
                // Increment the caseId until it is unique
                caseId = incrementCaseId(caseId);
            }
            incidentOrFraud.setCaseId(caseId);

            IncidentOrFraud newIncidentOrFraud = incidentOrFraudService.addIncidentFraudReport(incidentOrFraud);

            return new ResponseEntity<>(newIncidentOrFraud, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for detailed error information
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    // Helper method to increment the caseId

    private String incrementCaseId(String caseId) {
        String[] parts = caseId.split("/");
        int year = Integer.parseInt(parts[3]);
        int month = Integer.parseInt(parts[2]);
        int day = Integer.parseInt(parts[1]);
        int caseNumber = Integer.parseInt(parts[0]);

        // Increment the case number
        caseNumber++;

        // Reset the case number to 1 if the year has changed
        if (year > Integer.parseInt(parts[3])) {
            caseNumber = 1;
        }

        // Format the incremented values into the new caseId
        return String.format("%04d/%02d/%02d/%04d", caseNumber, day, month, year);

    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

//    @GetMapping("/image/{id}")
//    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR','ICMS_BRANCH_MANAGER')")
//    public Images getImage(@PathVariable Long id) throws IOException {
//        Images imageBytes = incidentOrFraudService.getDocument(id);
//       System.out.println(imageBytes.getFile().length);
//        return imageBytes;
//    }
//
//    @GetMapping("/image/{id}")
//    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR','ICMS_BRANCH_MANAGER')")
//    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
//        Images image = incidentOrFraudService.getDocument(id);
//        byte[] fileBytes = image.getFile();
//        String contentType = image.getContentType();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(contentType));
//        headers.setContentLength(fileBytes.length);
//        System.out.println(fileBytes.length);
//        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
//    }
@GetMapping("/image/{id}")
@PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR','ICMS_BRANCH_MANAGER')")
public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
    Images image = incidentOrFraudService.getDocument(id);
    byte[] fileBytes = image.getFile();
    String contentType = image.getContentType();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(contentType));
    headers.setContentLength(fileBytes.length);
//    System.out.println(contentType);
//    System.out.println(fileBytes.length);
    MediaType mediaType = MediaTypeFactory
            .getMediaType(contentType)
            .orElse(MediaType.APPLICATION_OCTET_STREAM);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + id)
            .contentType(mediaType)
            .contentLength(fileBytes.length)
            .body(fileBytes);
}

    @GetMapping("/images/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR','ICMS_BRANCH_MANAGER')")
    public ResponseEntity<ByteArrayResource> getImages(@PathVariable Long id) throws IOException {
        ByteArrayResource image = incidentOrFraudService.downloadDocFile(id);
        String filePath = incidentOrFraudService.getPath(id);

        //setting media type of the file
        MediaType mediaType = MediaTypeFactory
                .getMediaType(filePath)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        System.out.println(image.contentLength());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + filePath)
                .contentLength(image.contentLength())
                .contentType(mediaType)
                .body(image);
    }

    @PutMapping("/update")
   @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC')")
   public ResponseEntity<IncidentOrFraud> updateIncidentOrFraud(@RequestPart("incidentOrFraud") String incidentOrFraudJson,
                                                             @RequestPart(value = "file", required = false) MultipartFile file) {

    ObjectMapper objectMapper = new ObjectMapper();
    IncidentOrFraud incidentOrFraud;

    try {
        incidentOrFraud = objectMapper.readValue(incidentOrFraudJson, IncidentOrFraud.class);

        // Update the file only if a new file is provided
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = "uploads/" + fileName;
            Path path = Paths.get(filePath);

            try {
                Files.write(path, file.getBytes());
                incidentOrFraud.setFileName(fileName);
                incidentOrFraud.setFilePath(filePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        IncidentOrFraud updatedIncidentOrFraud = incidentOrFraudService.updateIncidentFraudReport(incidentOrFraud);
        return new ResponseEntity<>(updatedIncidentOrFraud, HttpStatus.CREATED);

    } catch (JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}

    @PatchMapping("/calculateProvision/{id}")
    @PreAuthorize("hasAnyRole('ICMS_PROVISION')")
    public ResponseEntity<IncidentOrFraud> calculateProvision(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            IncidentOrFraud row = incidentOrFraudService.calculateProvision(id, requestBody.get("provisionHeld"));
            return ResponseEntity.ok(row);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/authorize/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_DISTRICT_DIRECTOR','ICMS_ADMIN')")
    public ResponseEntity<IncidentOrFraud> updateTableRow(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            IncidentOrFraud row = incidentOrFraudService.updateTableRow(id, requestBody.get("authorizer"));
            return ResponseEntity.ok(row);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")


    public ResponseEntity<?> deleteIncidentFraudReport(@PathVariable("id") Long id) {
        incidentOrFraudService.deleteIncidentFraudReport(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getLastWeekCasesCount")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getIncidentOrFraudCasesLastWeekCount() {
        int count = incidentOrFraudService.findNumberOfIncidentOrFraudCasesLastWeek();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/getLastWeekCasesByFraudType")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Map<String, Integer>> getIncidentOrFraudCasesLastWeekByFraudType() {
        Map<String, Integer> countMap = incidentOrFraudService.findNumberOfIncidentOrFraudCasesLastWeekByFraudType();
        return new ResponseEntity<>(countMap, HttpStatus.OK);
    }

    @GetMapping("/getOutstandingCasesDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getOutstandingCasesDuringQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> outstandingCases = new ArrayList<>();

        // Calculate the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());


        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    caseItem.getCaseStatus().getName().equals("Outstanding")) {
                outstandingCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(outstandingCases.size(), HttpStatus.OK);
    }

    @GetMapping("/getOutstandingCasesDuringQuarter-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<IncidentOrFraud>> getOutstandingCasesDuringQuarterlist() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> outstandingCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    caseItem.getCaseStatus().getName().equals("Outstanding")) {
                outstandingCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(outstandingCases, HttpStatus.OK);
    }

    @GetMapping("/getClosedCasesDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getClosedCasesDuringQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> closedCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    caseItem.getCaseStatus().getName().equals("Closed")) {
                closedCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(closedCases.size(), HttpStatus.OK);
    }
    @GetMapping("/getClosedAndWrittenOffCasesDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getClosedAndWrittenOffCasesDuringQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> closedAndWrittenOffCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Closed" or "Written Off"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    (caseItem.getCaseStatus().getName().equals("Closed") ||
                            caseItem.getCaseStatus().getName().equals("Written Off"))) {
                closedAndWrittenOffCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(closedAndWrittenOffCases.size(), HttpStatus.OK);
    }
    @GetMapping("/getClosedAndWrittenOffCasesDuringQuarter-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<IncidentOrFraud>> getClosedAndWrittenOffCasesDuringQuarterlist() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> closedAndWrittenOffCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Closed" or "Written Off"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    (caseItem.getCaseStatus().getName().equals("Closed") ||
                            caseItem.getCaseStatus().getName().equals("Written Off"))) {
                closedAndWrittenOffCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(closedAndWrittenOffCases, HttpStatus.OK);
    }

    /*@GetMapping("/getClosedAndWrittenOffCasesDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getClosedAndWrittenOffCasesDuringQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> closedAndWrittenOffCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int yearOffset = 0;
        Month startMonth;
        if (currentDate.getMonthValue() >= 1 && currentDate.getMonthValue() <= 3) {
            startMonth = Month.JANUARY;
        } else if (currentDate.getMonthValue() >= 4 && currentDate.getMonthValue() <= 6) {
            startMonth = Month.APRIL;
        } else if (currentDate.getMonthValue() >= 7 && currentDate.getMonthValue() <= 9) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        if (startMonth.equals(Month.JANUARY)) {
            yearOffset = -1;
        }
        LocalDate startQuarter = LocalDate.of(currentDate.getYear() + yearOffset, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Closed" or "Written Off"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    (caseItem.getCaseStatus().getName().equals("Closed") ||
                            caseItem.getCaseStatus().getName().equals("Written Off"))) {
                closedAndWrittenOffCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(closedAndWrittenOffCases.size(), HttpStatus.OK);
    }

    @GetMapping("/getClosedAndWrittenOffCasesDuringQuarter-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<IncidentOrFraud>> getClosedAndWrittenOffCasesDuringQuarterlist() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> closedAndWrittenOffCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int yearOffset = 0;
        Month startMonth;
        if (currentDate.getMonthValue() >= 1 && currentDate.getMonthValue() <= 3) {
            startMonth = Month.JANUARY;
        } else if (currentDate.getMonthValue() >= 4 && currentDate.getMonthValue() <= 6) {
            startMonth = Month.APRIL;
        } else if (currentDate.getMonthValue() >= 7 && currentDate.getMonthValue() <= 9) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        if (startMonth.equals(Month.JANUARY)) {
            yearOffset = -1;
        }
        LocalDate startQuarter = LocalDate.of(currentDate.getYear() + yearOffset, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Closed" or "Written Off"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    (caseItem.getCaseStatus().getName().equals("Closed") ||
                            caseItem.getCaseStatus().getName().equals("Written Off"))) {
                closedAndWrittenOffCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(closedAndWrittenOffCases, HttpStatus.OK);
    }
*/

    @GetMapping("/getOutstandingCasesAmountDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<String> getOutstandingCasesAmountDuringQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> outstandingCases = new ArrayList<>();
        double totalAmount = 0;

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    caseItem.getCaseStatus().getName().equals("Outstanding")) {
                double fraudAmount = Double.parseDouble(caseItem.getFraudAmount().replaceAll(",", ""));
                totalAmount += fraudAmount;
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedAmount = decimalFormat.format(totalAmount);

        return new ResponseEntity<>(formattedAmount, HttpStatus.OK);
    }

    @GetMapping("/getOutstandingCasesInPreviousQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getOutstandingCasesInPreviousQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> outstandingCases = new ArrayList<>();

        // Get the start and end dates for the previous quarter
        LocalDate currentDate = LocalDate.now();
        int yearOffset = 0;
        Month startMonth = null;
        if (currentDate.getMonthValue() >= 1 && currentDate.getMonthValue() <= 3) {
            startMonth = Month.OCTOBER;
            yearOffset = -1;
        } else if (currentDate.getMonthValue() >= 4 && currentDate.getMonthValue() <= 6) {
            startMonth = Month.JANUARY;
        } else if (currentDate.getMonthValue() >= 7 && currentDate.getMonthValue() <= 9) {
            startMonth = Month.APRIL;
        } else {
            startMonth = Month.JULY;
        }

        LocalDate startQuarter = LocalDate.of(currentDate.getYear() + yearOffset, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the previous quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter.minusMonths(3)) && caseDate.isBefore(startQuarter) &&
                    caseItem.getCaseStatus().getName().equals("Outstanding")) {
                outstandingCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(outstandingCases.size(), HttpStatus.OK);
    }


    @GetMapping("/getOutstandingCasesInPreviousQuarter-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<IncidentOrFraud>> getOutstandingCasesInPreviousQuarterlist() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> outstandingCases = new ArrayList<>();

        // Get the start and end dates for the previous quarter
        LocalDate currentDate = LocalDate.now();
        int yearOffset = 0;
        Month startMonth = null;
        if (currentDate.getMonthValue() >= 1 && currentDate.getMonthValue() <= 3) {
            startMonth = Month.OCTOBER;
            yearOffset = -1;
        } else if (currentDate.getMonthValue() >= 4 && currentDate.getMonthValue() <= 6) {
            startMonth = Month.JANUARY;
        } else if (currentDate.getMonthValue() >= 7 && currentDate.getMonthValue() <= 9) {
            startMonth = Month.APRIL;
        } else {
            startMonth = Month.JULY;
        }

        LocalDate startQuarter = LocalDate.of(currentDate.getYear() + yearOffset, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the previous quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    caseItem.getCaseStatus().getName().equals("Outstanding")) {
                outstandingCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(outstandingCases, HttpStatus.OK);
    }

    @GetMapping("/getNewCasesDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getNewCasesDuringQuarter() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> newCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter.minusDays(1)) && caseDate.isBefore(endQuarter.plusDays(1))) {
                newCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(newCases.size(), HttpStatus.OK);
    }

    @GetMapping("/getNewCasesDuringQuarter-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<IncidentOrFraud>> getNewCasesDuringQuarterlist() {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> newCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter.minusDays(1)) && caseDate.isBefore(endQuarter.plusDays(1))) {
                newCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(newCases, HttpStatus.OK);
    }
}
