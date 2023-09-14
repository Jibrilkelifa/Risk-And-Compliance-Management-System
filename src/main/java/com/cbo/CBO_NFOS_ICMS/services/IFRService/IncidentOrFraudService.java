package com.cbo.CBO_NFOS_ICMS.services.IFRService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFR.CaseStatus;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository.IncidentOrFraudRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.OrganizationalUnitService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class IncidentOrFraudService {

    private final SubProcessService subProcessService;
    private final OrganizationalUnitService organizationalUnitService;
    private final IncidentOrFraudRepository incidentOrFraudRepository;

    public IncidentOrFraudService(SubProcessService subProcessService, OrganizationalUnitService organizationalUnitService, IncidentOrFraudRepository incidentOrFraudRepository) {
        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
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

    public IncidentOrFraud updateIncidentFraudReport(IncidentOrFraud IncidentOrFraud) {
        return incidentOrFraudRepository.save(IncidentOrFraud);
    }

    public IncidentOrFraud findIncidentFraudReportById(Long id) {
        return incidentOrFraudRepository.findIncidentFraudReportById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }
    public void deleteIncidentFraudReport(Long id) {
        incidentOrFraudRepository.deleteById(id);
    }

    public List<IncidentOrFraud> findAllIncidentFraudReportInSpecificOrganizationalUnit(Long id) {
        OrganizationalUnit organizationalUnit = organizationalUnitService.findOrganizationalUnitById(id);
        return incidentOrFraudRepository.findIncidentFraudReportByOrganizationalUnit(organizationalUnit);
    }

    public List<IncidentOrFraud> findAllIncidentFraudReportInSpecificSubProcess(Long id) {
        SubProcess subProcess = subProcessService.findSubProcessById(id);
        List<OrganizationalUnit> organizationalUnits = organizationalUnitService.findOrganizationalUnitBySubProcess(subProcess);
        List<IncidentOrFraud> ifrs = new ArrayList<>();
        for (int i = 0; i < organizationalUnits.size(); i++) {
            List<IncidentOrFraud> allBranches = incidentOrFraudRepository.findIncidentFraudReportByOrganizationalUnit(organizationalUnits.get(i));
            for (int j = 0; j < allBranches.size(); j++) {
                ifrs.add(incidentOrFraudRepository.findIncidentFraudReportByOrganizationalUnit(organizationalUnits.get(i)).get(j));
            }
        }
        return ifrs;
    }

    public IncidentOrFraud calculateProvision(Long id, String provisionHeld) {
        IncidentOrFraud row = incidentOrFraudRepository.findById(id).orElseThrow(()-> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setProvisionHeld(provisionHeld);
        return incidentOrFraudRepository.save(row);
    }

    public IncidentOrFraud updateTableRow(Long id, String caseAuthorizer) {
        IncidentOrFraud row = incidentOrFraudRepository.findById(id).orElseThrow(()-> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setAuthorizedBy(caseAuthorizer);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);

        row.setAuthorizationTimeStamp(formattedDate);
        return incidentOrFraudRepository.save(row);
    }
    public void deleteRow(int id) {
        Optional<IncidentOrFraud> data =  incidentOrFraudRepository.findById((long) id);
        if (data.isPresent()) {

            List<IncidentOrFraud> dataList =  incidentOrFraudRepository.findAll();
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
}


