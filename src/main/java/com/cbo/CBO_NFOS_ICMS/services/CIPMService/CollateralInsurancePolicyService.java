package com.cbo.CBO_NFOS_ICMS.services.CIPMService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.CollateralInsurancePolicyRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CollateralInsurancePolicyService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final SubProcessService subProcessService;
    private final BranchService organizationalUnitService;
    private final CollateralInsurancePolicyRepository collateralInsurancePolicyRepository;

    public CollateralInsurancePolicyService(SubProcessService subProcessService, BranchService organizationalUnitService, CollateralInsurancePolicyRepository collateralInsurancePolicyRepository) {
        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
        this.collateralInsurancePolicyRepository = collateralInsurancePolicyRepository;
    }

    public CollateralInsurancePolicy addCollateralInsurancePolicy(CollateralInsurancePolicy CollateralInsurancePolicy) {

        return collateralInsurancePolicyRepository.save(CollateralInsurancePolicy);
    }

    public List<CollateralInsurancePolicy> findAllCollateralInsurancePolicy() {
        return collateralInsurancePolicyRepository.findAll();
    }

    public CollateralInsurancePolicy updateColateralInsurancePolicy(CollateralInsurancePolicy collateralInsurancePolicy) {

        Optional<CollateralInsurancePolicy> optionalCollateralInsurancePolicy = collateralInsurancePolicyRepository.findById(collateralInsurancePolicy.getId());
        if (optionalCollateralInsurancePolicy.isPresent()) {
            CollateralInsurancePolicy existingCollateralInsurance = optionalCollateralInsurancePolicy.get();
            existingCollateralInsurance.setStatus(collateralInsurancePolicy.getStatus());
            existingCollateralInsurance.setInsuranceExpireDate(collateralInsurancePolicy.getInsuranceExpireDate());
            return collateralInsurancePolicyRepository.save(existingCollateralInsurance);
        } else {
            throw new ResourceNotFoundException("Collateral Insurance Policy not found");
        }
    }

    public CollateralInsurancePolicy updateCollateralInsurancePolicy(CollateralInsurancePolicy collateralInsurancePolicy) {
        collateralInsurancePolicy.setIsAuthorized(false);
        System.out.println(collateralInsurancePolicy.getStatus());
        return collateralInsurancePolicyRepository.save(collateralInsurancePolicy);


    }


    public CollateralInsurancePolicy findCollateralInsurancePolicyById(Long id) {
        return collateralInsurancePolicyRepository.findCollateralInsurancePolicyById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteCollateralInsurancePolicy(Long id) {
        collateralInsurancePolicyRepository.deleteById(id);
    }

    public List<CollateralInsurancePolicy> findAllCollateralInsurancePolicyInSpecificOrganizationalUnit(String branchId) {
//        Branch branch = organizationalUnitService.findBranchById(id);
        return collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);
    }

    public List<CollateralInsurancePolicy> findAllCollateralInsurancePolicyInSpecificSubProcess(Long subProcessId) {
//        Branch branch = organizationalUnitService.findBranchById(id);
        return collateralInsurancePolicyRepository.findCollateralInsurancePolicyBySubProcessId(subProcessId);
    }

    public Long getTotalNumberOfPolicies() {
        return collateralInsurancePolicyRepository.count();
    }

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

    public List<CollateralInsurancePolicy> getExpiredPolicies() {
        LocalDate currentDate = LocalDate.now();
        List<CollateralInsurancePolicy> allPolicies = collateralInsurancePolicyRepository.findAll();
        List<CollateralInsurancePolicy> expiredPolicies = new ArrayList<>();
        for (CollateralInsurancePolicy policy : allPolicies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
            if (expiryDate.isBefore(currentDate)) {
                expiredPolicies.add(policy);
            }
        }
        return expiredPolicies;
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

    public List<CollateralInsurancePolicy> getPoliciesExpiringWithinThirtyDays() {
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysFromNow = currentDate.plusDays(30);
        List<CollateralInsurancePolicy> allPolicies = collateralInsurancePolicyRepository.findAll();
        List<CollateralInsurancePolicy> expiringPolicies = new ArrayList<>();

        for (CollateralInsurancePolicy policy : allPolicies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
            if (expiryDate.isAfter(currentDate) && expiryDate.isBefore(thirtyDaysFromNow)) {
                expiringPolicies.add(policy);
            }
        }

        return expiringPolicies;
    }

    public List<Object[]> getExpiredPoliciesCountBySubProcess(LocalDate expirationDate) {
        List<SubProcess> subProcesses = subProcessService.findAllSubProcess();
        List<Object[]> expiredPoliciesCountBySubProcess = new ArrayList<>();
        for (SubProcess subProcess : subProcesses) {
            List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyBySubProcessId(subProcess.getId());
            int expiredPoliciesCount = 0;
            for (CollateralInsurancePolicy policy : policies) {
                LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
                if (expiryDate.isBefore(expirationDate)) {
                    expiredPoliciesCount++;
                }
            }
            Object[] row = {subProcess, expiredPoliciesCount};
            expiredPoliciesCountBySubProcess.add(row);
        }
        return expiredPoliciesCountBySubProcess;
    }

    public CollateralInsurancePolicy authorizeIFR(Long id, String caseAuthorizer) {
        CollateralInsurancePolicy row = collateralInsurancePolicyRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setAuthorizedBy(caseAuthorizer);
        row.setIsAuthorized(true);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);

        row.setAuthorizationTimeStamp(formattedDate);
        return collateralInsurancePolicyRepository.save(row);
    }

}