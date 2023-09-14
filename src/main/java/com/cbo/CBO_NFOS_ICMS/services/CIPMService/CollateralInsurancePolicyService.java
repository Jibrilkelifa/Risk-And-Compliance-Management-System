package com.cbo.CBO_NFOS_ICMS.services.CIPMService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.CollateralInsurancePolicyRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.OrganizationalUnitService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class CollateralInsurancePolicyService {

    private final SubProcessService subProcessService;
    private  final OrganizationalUnitService organizationalUnitService;
    private final CollateralInsurancePolicyRepository collateralInsurancePolicyRepository;

    public CollateralInsurancePolicyService(SubProcessService subProcessService, OrganizationalUnitService organizationalUnitService, CollateralInsurancePolicyRepository collateralInsurancePolicyRepository) {
        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
        this.collateralInsurancePolicyRepository = collateralInsurancePolicyRepository;
    }
    public CollateralInsurancePolicy addCollateralInsurancePolicy (CollateralInsurancePolicy CollateralInsurancePolicy){

        return collateralInsurancePolicyRepository.save(CollateralInsurancePolicy);
    }
    public List<CollateralInsurancePolicy> findAllCollateralInsurancePolicy(){
        return collateralInsurancePolicyRepository.findAll();
    }
    public CollateralInsurancePolicy updateCollateralInsurancePolicy(CollateralInsurancePolicy CollateralInsurancePolicy){
        return collateralInsurancePolicyRepository.save(CollateralInsurancePolicy);
    }
    public CollateralInsurancePolicy findCollateralInsurancePolicyById(Long id){
        return collateralInsurancePolicyRepository.findCollateralInsurancePolicyById(id)
                .orElseThrow(()-> new UserNotFoundException("User by id" + id + " was not found"));
    }
    public void deleteCollateralInsurancePolicy(Long id){
        collateralInsurancePolicyRepository.deleteById(id);
    }

    public List<CollateralInsurancePolicy> findAllCollateralInsurancePolicyInSpecificOrganizationalUnit(Long id) {
        OrganizationalUnit organizationalUnit = organizationalUnitService.findOrganizationalUnitById(id);
        return collateralInsurancePolicyRepository.findCollateralInsurancePolicyByOrganizationalUnit(organizationalUnit);
    }

    public List<CollateralInsurancePolicy> findAllCollateralInsurancePolicyInSpecificSubProcess(Long id) {
        SubProcess subProcess = subProcessService.findSubProcessById(id);
        List<OrganizationalUnit> organizationalUnits = organizationalUnitService.findOrganizationalUnitBySubProcess(subProcess);
        List<CollateralInsurancePolicy> CollateralInsurancePolicys = new ArrayList<>();
        for (int i=0; i < organizationalUnits.size(); i++ ){
            List<CollateralInsurancePolicy>  clmfjhd = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByOrganizationalUnit(organizationalUnits.get(i));
            for(int j=0; j < clmfjhd.size(); j++ ){
                CollateralInsurancePolicys.add(collateralInsurancePolicyRepository.findCollateralInsurancePolicyByOrganizationalUnit(organizationalUnits.get(i)).get(j));
            }
        }
        return CollateralInsurancePolicys;
    }
    public Long getTotalNumberOfPolicies() {
        return collateralInsurancePolicyRepository.count();
    }
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");


    public int getNumberOfExpiredPolicies() {
        LocalDate currentDate = LocalDate.now();
        List<CollateralInsurancePolicy> allPolicies = collateralInsurancePolicyRepository.findAll();
        List<CollateralInsurancePolicy> expiredPolicies = new ArrayList<>();
        for (CollateralInsurancePolicy policy : allPolicies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
            if (expiryDate.isBefore(currentDate)) {
                expiredPolicies.add(policy);
            }
        }
        return expiredPolicies.size();
    }
    public int getNumberOfPoliciesExpiringWithinThirtyDays() {
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysFromNow = currentDate.plusDays(30);
        List<CollateralInsurancePolicy> allPolicies = collateralInsurancePolicyRepository.findAll();
        int count = 0;
        for (CollateralInsurancePolicy policy : allPolicies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
            if (expiryDate.isAfter(currentDate) && expiryDate.isBefore(thirtyDaysFromNow)) {
                count++;
            }
        }
        return count;
    }
    public List<Object[]> getExpiredPoliciesCountBySubProcess(LocalDate expirationDate) {
        List<SubProcess> subProcesses = subProcessService.findAllSubProcess();
        List<Object[]> expiredPoliciesCountBySubProcess = new ArrayList<>();
        for (SubProcess subProcess : subProcesses) {
            List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByOrganizationalUnit_SubProcess(subProcess);
            int expiredPoliciesCount = 0;
            for (CollateralInsurancePolicy policy : policies) {
                LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
                if (expiryDate.isBefore(expirationDate)) {
                    expiredPoliciesCount++;
                }
            }
            Object[] row = { subProcess, expiredPoliciesCount };
            expiredPoliciesCountBySubProcess.add(row);
        }
        return expiredPoliciesCountBySubProcess;
    }

    public CollateralInsurancePolicy authorizeIFR(Long id, String caseAuthorizer) {
        CollateralInsurancePolicy row = collateralInsurancePolicyRepository.findById(id).orElseThrow(()-> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setAuthorizedBy(caseAuthorizer);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);

        row.setAuthorizationTimeStamp(formattedDate);
        return collateralInsurancePolicyRepository.save(row);
    }
}
