package com.cbo.CBO_NFOS_ICMS.services.IFRService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.Images;
import com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository.IncidentOrFraudRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class IncidentOrFraudService {

    private final SubProcessService subProcessService;
    private final BranchService branchService;
    private final IncidentOrFraudRepository incidentOrFraudRepository;

    public IncidentOrFraudService(SubProcessService subProcessService, BranchService branchService, IncidentOrFraudRepository incidentOrFraudRepository) {
        this.subProcessService = subProcessService;
        this.branchService = branchService;
        this.incidentOrFraudRepository = incidentOrFraudRepository;
    }

    public IncidentOrFraud addIncidentFraudReport(IncidentOrFraud incidentOrFraud) {
        return incidentOrFraudRepository.save(incidentOrFraud);
    }

    public List<IncidentOrFraud> findAllIncidentFraudReport() {
        return incidentOrFraudRepository.findAll();
    }

    public int findIncidentFraudReportSize() {
        return incidentOrFraudRepository.findAll().size();
    }

    public IncidentOrFraud updateIncidentFraudReport(IncidentOrFraud incidentOrFraud) {

//      incidentOrFraud.setIsAuthorized(false);
        Optional<IncidentOrFraud> optionalIncidentOrFraud = incidentOrFraudRepository.findById(incidentOrFraud.getId());
        if (optionalIncidentOrFraud.isPresent()) {
            IncidentOrFraud existingIncidentOrFraud = optionalIncidentOrFraud.get();
            existingIncidentOrFraud.setCaseStatus(incidentOrFraud.getCaseStatus());
            existingIncidentOrFraud.setAmountRecovered(incidentOrFraud.getAmountRecovered());
            existingIncidentOrFraud.setIsAuthorized(false);
            return incidentOrFraudRepository.save(existingIncidentOrFraud);
        } else {
            throw new ResourceNotFoundException("Incident or Fraud not found");
        }
    }

    public Images getImage(Long id) throws IOException {

        IncidentOrFraud fraud = incidentOrFraudRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fraud not found with id " + id));
        if (fraud != null) {

            BufferedImage stampImage = ImageIO.read(new File(fraud.getFilePath()));
            ByteArrayOutputStream stbos = new ByteArrayOutputStream();
            ImageIO.write(stampImage, "png", stbos);
            byte[] stadata = stbos.toByteArray();
            Images images = new Images();
            images.setFile(stadata);
            return images;
        }

        return null;

    }

    public IncidentOrFraud findIncidentFraudReportById(Long id) {
        return incidentOrFraudRepository.findIncidentFraudReportById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteIncidentFraudReport(Long id) {
        incidentOrFraudRepository.deleteById(id);
    }

    public List<IncidentOrFraud> findAllIncidentFraudReportInSpecificOrganizationalUnit(Long branchId) {

        return incidentOrFraudRepository.findIncidentFraudReportByBranchId(branchId);
    }

    public List<IncidentOrFraud> findAllIncidentFraudReportInSpecificSubProcess(Long SubProcessId) {

        return incidentOrFraudRepository.findIncidentFraudReportBySubProcessId(SubProcessId);
    }

//    public List<IncidentOrFraud> findAllIncidentFraudReportInSpecificSubProcess(Long id) {
//        SubProcess subProcess = subProcessService.findSubProcessById(id);
//        List<Branch> organizationalUnits = branchService.findBranchBySubProcess(subProcess);
//        List<IncidentOrFraud> ifrs = new ArrayList<>();
//        for (int i = 0; i < organizationalUnits.size(); i++) {
//            List<IncidentOrFraud> allBranches = incidentOrFraudRepository.findIncidentFraudReportByBranch(organizationalUnits.get(i));
//            for (int j = 0; j < allBranches.size(); j++) {
//                ifrs.add(incidentOrFraudRepository.findIncidentFraudReportByBranch(organizationalUnits.get(i)).get(j));
//            }
//        }
//        return ifrs;
//    }

    public IncidentOrFraud calculateProvision(Long id, String provisionHeld) {
        IncidentOrFraud row = incidentOrFraudRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setProvisionHeld(provisionHeld);
        return incidentOrFraudRepository.save(row);
    }

    public IncidentOrFraud updateTableRow(Long id, String caseAuthorizer) {
        IncidentOrFraud row = incidentOrFraudRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setAuthorizedBy(caseAuthorizer);
        row.setIsAuthorized(true);
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);

        row.setAuthorizationTimeStamp(formattedDate);
        return incidentOrFraudRepository.save(row);
    }

    public void deleteRow(int id) {
        Optional<IncidentOrFraud> data = incidentOrFraudRepository.findById((long) id);
        if (data.isPresent()) {

            List<IncidentOrFraud> dataList = incidentOrFraudRepository.findAll();
            for (int i = id; i - 1 < dataList.size(); i++) {
                IncidentOrFraud d = dataList.get(i - 1);

                d.setId(d.getId() - 1);
                incidentOrFraudRepository.save(d);
            }
            incidentOrFraudRepository.deleteById((long) id);
        }
    }

    public int findNumberOfIncidentOrFraudCasesLastWeek() {
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int count = incidentOrFraudRepository.countAllByFraudDetectionDateBetween(
                startDate.format(formatter), endDate.format(formatter));
        return count;
    }

    public Map<String, Integer> findNumberOfIncidentOrFraudCasesLastWeekByFraudType() {
        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Object[]> results = incidentOrFraudRepository.findNumberOfIncidentOrFraudCasesLastWeekByFraudType(
                startDate.format(formatter), endDate.format(formatter));
        Map<String, Integer> countMap = new HashMap<>();
        for (Object[] result : results) {
            String fraudType = (String) result[0];
            int count = ((Number) result[1]).intValue();
            countMap.put(fraudType, count);
        }
        return countMap;
    }

    public void processIncidentFraudReport(MultipartFile file) throws IOException {
        // Save the file to the desired location or perform any processing/logic
        String filename = file.getOriginalFilename();
        byte[] fileData = file.getBytes();

        // Example: Saving the file to the database
        IncidentOrFraud incidentOrFraud = new IncidentOrFraud();
        incidentOrFraud.setFileName(filename);
        incidentOrFraud.setFileData(fileData);
        incidentOrFraudRepository.save(incidentOrFraud);
    }
}


