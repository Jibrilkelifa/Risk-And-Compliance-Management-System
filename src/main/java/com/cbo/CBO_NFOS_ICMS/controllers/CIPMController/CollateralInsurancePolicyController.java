package com.cbo.CBO_NFOS_ICMS.controllers.CIPMController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.CollateralInsurancePolicyService;

import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController
@RequestMapping("CIPM")
public class CollateralInsurancePolicyController {
    private final CollateralInsurancePolicyService collateralInsurancePolicyService;
    private final SubProcessService subProcessService;

    public CollateralInsurancePolicyController(CollateralInsurancePolicyService collateralInsurancePolicyService, SubProcessService subProcessService) {
        this.collateralInsurancePolicyService = collateralInsurancePolicyService;
        this.subProcessService = subProcessService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<CollateralInsurancePolicy>> getAllCollateralInsurancePolicy() {
        List<CollateralInsurancePolicy> CollateralInsurancePolicy = collateralInsurancePolicyService.findAllCollateralInsurancePolicy();
        return new ResponseEntity<>(CollateralInsurancePolicy, HttpStatus.OK);
    }

    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC', 'ICMS_BRANCH_MANAGER')")
    public ResponseEntity<List<CollateralInsurancePolicy>> getAllCollateralInsurancePolicyInSpecificOrganizationalUnit(@PathVariable("id") String branchId) {
        List<CollateralInsurancePolicy> CollateralInsurancePolicy;
        CollateralInsurancePolicy = collateralInsurancePolicyService.findAllCollateralInsurancePolicyInSpecificOrganizationalUnit(branchId);
        return new ResponseEntity<>(CollateralInsurancePolicy, HttpStatus.OK);
    }

    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR')")
    public ResponseEntity<List<CollateralInsurancePolicy>> getAllCollateralInsurancePolicyInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<CollateralInsurancePolicy> CollateralInsurancePolicy;
        CollateralInsurancePolicy = collateralInsurancePolicyService.findAllCollateralInsurancePolicyInSpecificSubProcess(subProcessId);
        return new ResponseEntity<>(CollateralInsurancePolicy, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CollateralInsurancePolicy> getCollateralInsurancePolicyId
            (@PathVariable("id") Long id) {
        CollateralInsurancePolicy CollateralInsurancePolicy = collateralInsurancePolicyService.findCollateralInsurancePolicyById(id);
        return new ResponseEntity<>(CollateralInsurancePolicy, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<CollateralInsurancePolicy> addCollateralInsurancePolicy
            (@RequestBody CollateralInsurancePolicy collateralInsurancePolicy) {

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);
        collateralInsurancePolicy.setPreparationTimeStamp(formattedDate);

        CollateralInsurancePolicy newCollateralInsurancePolicy = collateralInsurancePolicyService.addCollateralInsurancePolicy(collateralInsurancePolicy);
        return new ResponseEntity<>(newCollateralInsurancePolicy, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<CollateralInsurancePolicy> updateEmloyee
            (@RequestBody CollateralInsurancePolicy collateralInsurancePolicy) {
        System.out.println(collateralInsurancePolicy.getStatus());
        CollateralInsurancePolicy updateCollateralInsurancePolicy = collateralInsurancePolicyService.updateCollateralInsurancePolicy(collateralInsurancePolicy);
        return new ResponseEntity<>(updateCollateralInsurancePolicy, HttpStatus.CREATED);

    }

/*        @PutMapping("/update")
        @PreAuthorize("hasRole('ICMS_BRANCH')")
        public ResponseEntity<CollateralInsurancePolicy> updateEmloyee
        (@RequestBody CollateralInsurancePolicy collateralInsurancePolicy){
            System.out.println(collateralInsurancePolicy.getStatus());
            CollateralInsurancePolicy updateCollateralInsurancePolicy = collateralInsurancePolicyService.updateCollateralInsurancePolicy(collateralInsurancePolicy);
            return new ResponseEntity<>(updateCollateralInsurancePolicy, HttpStatus.CREATED);


        }*/
    @PatchMapping("/authorize/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER')")
    public ResponseEntity<CollateralInsurancePolicy> updateTableRow(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            CollateralInsurancePolicy row = collateralInsurancePolicyService.authorizeIFR(id, requestBody.get("authorizer"));
            return ResponseEntity.ok(row);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")

    public ResponseEntity<?> deleteCollateralInsurancePolicy(@PathVariable("id") Long id) {
        collateralInsurancePolicyService.deleteCollateralInsurancePolicy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getTotalNumberOfPolicies")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Long> getTotalNumberOfPolicies() {
        Long totalNumberOfPolicies = collateralInsurancePolicyService.getTotalNumberOfPolicies();
        return new ResponseEntity<>(totalNumberOfPolicies, HttpStatus.OK);
/*        @GetMapping("/TotalNumberOfExpiredPolicies")
        @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
        public ResponseEntity<Integer> getNumberOfExpiredPolicies() {
            int numberOfExpiredPolicies = collateralInsurancePolicyService.getNumberOfExpiredPolicies();
            return new ResponseEntity<>(numberOfExpiredPolicies, HttpStatus.OK);
        }
    @GetMapping("/ExpiredPolicies")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<CollateralInsurancePolicy>> getExpiredPolicies() {
        List<CollateralInsurancePolicy> expiredPolicies = collateralInsurancePolicyService.getExpiredPolicies();
        return new ResponseEntity<>(expiredPolicies, HttpStatus.OK);*/

    }

    @GetMapping("/TotalNumberOfExpiredPolicies")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getNumberOfExpiredPolicies() {
        int numberOfExpiredPolicies = collateralInsurancePolicyService.getNumberOfExpiredPolicies();
        return new ResponseEntity<>(numberOfExpiredPolicies, HttpStatus.OK);
    }

    @GetMapping("/ExpiredPolicies")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<CollateralInsurancePolicy>> getExpiredPolicies() {
        List<CollateralInsurancePolicy> expiredPolicies = collateralInsurancePolicyService.getExpiredPolicies();
        return new ResponseEntity<>(expiredPolicies, HttpStatus.OK);
    }

    @GetMapping("/expiring-within-thirty-days")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getNumberOfPoliciesExpiringWithinThirtyDays() {
        int numberOfPoliciesExpiringWithinThirtyDays = collateralInsurancePolicyService.getNumberOfPoliciesExpiringWithinThirtyDays();
        return new ResponseEntity<>(numberOfPoliciesExpiringWithinThirtyDays, HttpStatus.OK);
    }

    @GetMapping("/expiring-within-thirty-days-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<CollateralInsurancePolicy>> getPoliciesExpiringWithinThirtyDays() {
        List<CollateralInsurancePolicy> policiesExpiringWithinThirtyDays = collateralInsurancePolicyService.getPoliciesExpiringWithinThirtyDays();
        return new ResponseEntity<>(policiesExpiringWithinThirtyDays, HttpStatus.OK);
    }

//    @GetMapping("/expiring-within-thirty-days-list")
//    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
//    public ResponseEntity<List<CollateralInsurancePolicy>> getPoliciesExpiringWithinThirtyDays() {
//        List<CollateralInsurancePolicy> policiesExpiringWithinThirtyDays = collateralInsurancePolicyService.getPoliciesExpiringWithinThirtyDays();
//        return new ResponseEntity<>(policiesExpiringWithinThirtyDays, HttpStatus.OK);
//    }

    @GetMapping("/perDistrict/expired")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Object[][]> getExpiredPoliciesCountByDistrict(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate) {

        // Fetch district list from the endpoint
        String[] districts = subProcessService.findAllDistricts();

        // Fetch expired policies count by subprocess
        List<Object[]> expiredPoliciesCount = collateralInsurancePolicyService.getExpiredPoliciesCountBySubProcess(expirationDate);

        // Filter policies by district
        List<Object[]> filteredPoliciesCount = new ArrayList<>();

        for (Object[] policyCount : expiredPoliciesCount) {
            SubProcess subProcess = (SubProcess) policyCount[0];
            String policyDistrict = subProcess.getName();

            if (Arrays.asList(districts).contains(policyDistrict)) {
                filteredPoliciesCount.add(policyCount);
            }
        }

        // Prepare response array
        Object[][] expiredPoliciesCountArray = new Object[filteredPoliciesCount.size()][2];

        for (int i = 0; i < filteredPoliciesCount.size(); i++) {
            expiredPoliciesCountArray[i][0] = ((SubProcess) filteredPoliciesCount.get(i)[0]).getName();
            expiredPoliciesCountArray[i][1] = (Integer) filteredPoliciesCount.get(i)[1];
        }

        return ResponseEntity.ok(expiredPoliciesCountArray);
    }

    @GetMapping("/perDistrict/expiredToday")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getExpiredPoliciesCountByDistrict() {
        // Get the current date
        LocalDate expirationDate = LocalDate.now();

        // Fetch district list from the endpoint
        String[] districts = subProcessService.findAllDistricts();

        // Fetch expired policies count by subprocess
        List<Object[]> expiredPoliciesCount = collateralInsurancePolicyService.getExpiredPoliciesCountBySubProcess(expirationDate);

        // Filter policies by district
        List<Object[]> filteredPoliciesCount = new ArrayList<>();

        for (Object[] policyCount : expiredPoliciesCount) {
            SubProcess subProcess = (SubProcess) policyCount[0];
            String policyDistrict = subProcess.getName();

            if (Arrays.asList(districts).contains(policyDistrict)) {
                filteredPoliciesCount.add(policyCount);
            }
        }

        // Prepare response list of key-value pairs
        List<Map<String, Object>> expiredPoliciesCountList = new ArrayList<>();

        for (Object[] policyCount : filteredPoliciesCount) {
            SubProcess subProcess = (SubProcess) policyCount[0];
            Integer count = (Integer) policyCount[1];

            Map<String, Object> map = new HashMap<>();
            map.put("districtName", subProcess.getName());
            map.put("noOfExpiredPolicies", count);

            expiredPoliciesCountList.add(map);
        }

        return ResponseEntity.ok(expiredPoliciesCountList);
    }
}

