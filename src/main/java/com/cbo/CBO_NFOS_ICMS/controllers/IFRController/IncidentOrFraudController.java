package com.cbo.CBO_NFOS_ICMS.controllers.IFRController;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.Images;
import com.cbo.CBO_NFOS_ICMS.services.IFRService.IncidentOrFraudService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Map;
@RestController
@RequestMapping("/incidentFraudReport")
public class IncidentOrFraudController {
    private final IncidentOrFraudService incidentOrFraudService;

    public IncidentOrFraudController(IncidentOrFraudService incidentOrFraudService) {
        this.incidentOrFraudService = incidentOrFraudService;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'ICMS_PROVISION')")
    public ResponseEntity<List<IncidentOrFraud>> getAllIncidentFraudReport(){
        List<IncidentOrFraud> IncidentOrFraud = incidentOrFraudService.findAllIncidentFraudReport();
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC')")
    public ResponseEntity<String> uploadForWrittenOff(@RequestParam("writtenOff") MultipartFile file) {
        try {
            // Validate file size, type, or perform any other necessary checks

            // Save the file and process it using the service
            incidentOrFraudService.processIncidentFraudReport(file);

            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
        }
    }
    @GetMapping("/getSize")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN')")
    public int  getIncidentFraudReportSize(){
        return incidentOrFraudService.findIncidentFraudReportSize();
    }


    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC', 'ICMS_PROVISION')")
    public ResponseEntity<List<IncidentOrFraud>> getAllIncidentFraudReportInSpecificOrganizationalUnit(@PathVariable("id") Long branchId) {
        List<IncidentOrFraud> IncidentOrFraud;
        IncidentOrFraud = incidentOrFraudService.findAllIncidentFraudReportInSpecificOrganizationalUnit(branchId);
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }
    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC')")
    public ResponseEntity<List<IncidentOrFraud>> getAllIncidentFraudReportInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<IncidentOrFraud> IncidentOrFraud;
        IncidentOrFraud = incidentOrFraudService.findAllIncidentFraudReportInSpecificSubProcess(subProcessId);
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<IncidentOrFraud> getIncidentFraudReportId
            (@PathVariable("id") Long id){
        IncidentOrFraud IncidentOrFraud = incidentOrFraudService.findIncidentFraudReportById(id);
        return new ResponseEntity<>(IncidentOrFraud, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC','ICMS_DISTRICT_IC','ICMS_ADMIN')")
    public ResponseEntity<IncidentOrFraud> addIncidentFraudReport(
            @RequestPart("incidentOrFraud") String incidentOrFraudJson,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) { // Add Authentication parameter

        ObjectMapper objectMapper = new ObjectMapper();
        IncidentOrFraud incidentOrFraud;

        try {
            incidentOrFraud = objectMapper.readValue(incidentOrFraudJson, IncidentOrFraud.class);

            // Set the addedByRole property based on the role of the user
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            incidentOrFraud.setAddedByRole(role);
            System.out.println("Added by role: " + role);


        }
        catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            // Save the file to the upload directory
            String fileName = file.getOriginalFilename();
            String filePath = "src/uploads/" + fileName;
            Path path = Paths.get(filePath);
            Files.write(path, file.getBytes());

            // Set the file information in the incident or fraud report object
            incidentOrFraud.setFileName(fileName);
            incidentOrFraud.setFilePath(filePath);

            // Save the incident or fraud report in the database
            IncidentOrFraud newIncidentOrFraud = incidentOrFraudService.addIncidentFraudReport(incidentOrFraud);

            return new ResponseEntity<>(newIncidentOrFraud, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @GetMapping("/image/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC')")
    public Images getImage(@PathVariable Long id) throws IOException {
        Images imageBytes = incidentOrFraudService.getImage(id);
//        System.out.println(imageBytes.getFile().length);
        return imageBytes;
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_IC')")
    public ResponseEntity<IncidentOrFraud> updateIncidentOrFraud
            (@RequestBody IncidentOrFraud incidentOrFraud){
        IncidentOrFraud updateIncidentOrFraud = incidentOrFraudService.updateIncidentFraudReport(incidentOrFraud);
        return new ResponseEntity<>(updateIncidentOrFraud, HttpStatus.CREATED);
    }
    @PatchMapping("/calculateProvision/{id}")
    @PreAuthorize("hasAnyRole('ICMS_PROVISION')")
    public ResponseEntity<IncidentOrFraud> calculateProvision(@PathVariable Long id, @RequestBody Map<String, String> requestBody)
    {
        try
        {
            IncidentOrFraud row = incidentOrFraudService.calculateProvision(id,  requestBody.get("provisionHeld"));
            return ResponseEntity.ok(row);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/authorize/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_DISTRICT_DIRECTOR','ICMS_ADMIN')")
    public ResponseEntity<IncidentOrFraud> updateTableRow(@PathVariable Long id, @RequestBody Map<String, String> requestBody)
    { try { IncidentOrFraud row = incidentOrFraudService.updateTableRow(id,  requestBody.get("authorizer"));
        return ResponseEntity.ok(row); } catch (Exception e)
    { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); } }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")


    public ResponseEntity<?> deleteIncidentFraudReport (@PathVariable("id") Long id){
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

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int yearOffset = 0;
        Month startMonth = null;
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

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
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
        int yearOffset = 0;
        Month startMonth = null;
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

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
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
        int yearOffset = 0;
        Month startMonth = null;
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

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
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


    @GetMapping("/getOutstandingCasesAmountDuringQuarter")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<String> getOutstandingCasesAmountDuringQuarter()  {
        List<IncidentOrFraud> allCases = incidentOrFraudService.findAllIncidentFraudReport();
        List<IncidentOrFraud> outstandingCases = new ArrayList<>();
        double totalAmount = 0;

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int yearOffset = 0;
        Month startMonth = null;
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

        // Filter the cases that occurred during the current quarter and have a status of "Outstanding"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
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
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
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
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
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
        int yearOffset = 0;
        Month startMonth = null;
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

        // Filter the cases that occurred during the current quarter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
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
        int yearOffset = 0;
        Month startMonth = null;
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

        // Filter the cases that occurred during the current quarter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(),formatter);
            if (caseDate.isAfter(startQuarter.minusDays(1)) && caseDate.isBefore(endQuarter.plusDays(1))) {
                newCases.add(caseItem);
            }
        }

        return new ResponseEntity<>(newCases, HttpStatus.OK);
    }
}
