package com.cbo.CBO_NFOS_ICMS.controllers.DACGMController;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.services.DACGMService.DailyActivityGapControlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DACGM")
public class DailyActivityGapController {
    private final DailyActivityGapControlService dACGMService;

    public DailyActivityGapController(DailyActivityGapControlService dACGMService) {
        this.dACGMService = dACGMService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<DailyActivityGapControl>> getAllDACGM() {
        List<DailyActivityGapControl> dACGM = dACGMService.findAllDACGM();
        return new ResponseEntity<>(dACGM, HttpStatus.OK);
    }


    @GetMapping("/getSize")
    public Long getDACGMSize(){

        return dACGMService.findDACGMSize();
    }

    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC')")
    public ResponseEntity<List<DailyActivityGapControl>> getAllDACGMInSpecificOrganizationalUnit(@PathVariable("id") String id) {
        List<DailyActivityGapControl> dACGM;
        dACGM = dACGMService.findAllDACGMInSpecificOrganizationalUnit(id);
        return new ResponseEntity<>(dACGM, HttpStatus.OK);
    }

    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR')")
    public ResponseEntity<List<DailyActivityGapControl>> getAllDailyActivityGapInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<DailyActivityGapControl> DailyActivityGapControl;
        DailyActivityGapControl = dACGMService.findAllDACGMInSpecificSubProcess(subProcessId);
        return new ResponseEntity<>(DailyActivityGapControl, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DailyActivityGapControl> getDACGMId
            (@PathVariable("id") Long id) {
        DailyActivityGapControl dACGM = dACGMService.findDACGMById(id);
        return new ResponseEntity<>(dACGM, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<DailyActivityGapControl> addDACGM
            (@RequestBody DailyActivityGapControl dACGM) {
        String caseId = dACGM.getCaseId();
        while (dACGMService.isCaseIdExists(caseId)) {
            // Increment the caseId until it is unique
            caseId = incrementCaseId(caseId);
        }
        dACGM.setCaseId(caseId);
        System.out.println(caseId);
        DailyActivityGapControl newDailyActivityGapControl = dACGMService.addDACGM(dACGM);
        return new ResponseEntity<>(newDailyActivityGapControl, HttpStatus.CREATED);
    }
    @GetMapping("/subcategories/count/by-branch/{branchId}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<Map<String, Long>> getDacgmNumbersBySubCategoryAndBranch(@PathVariable("branchId") String branchId) {
        Map<String,Long> subCategoryCounts = dACGMService.getDacgmNumbersBySubCategoryAndBranch(branchId);
        return ResponseEntity.ok(subCategoryCounts);
    }

    @GetMapping("/subcategories/count/by-subprocess/{subProcessId}")
    @PreAuthorize("hasRole('ICMS_DISTRICT_IC')")
    public ResponseEntity<Map<String, Long>> getDacgmNumbersBySubCategoryAndSubProcess(@PathVariable("subProcessId") Long subProcessId) {
        Map<String, Long> subCategoryCounts = dACGMService.getDacgmNumbersBySubCategoryAndSubProcess(subProcessId);
        return ResponseEntity.ok(subCategoryCounts);
    }

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
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<DailyActivityGapControl> updateDACGM
            (@RequestBody DailyActivityGapControl dACGM) {
        DailyActivityGapControl updateDailyActivityGapControl = dACGMService.updateDACGM(dACGM);
        return new ResponseEntity<>(updateDailyActivityGapControl, HttpStatus.CREATED);
    }

    //    @PutMapping("/approve/{id}")
//    @PreAuthorize("hasRole('ICMS_BRANCH_MANAGER')")
//    public ResponseEntity<DailyActivityGapControl> approveDACGM
//            (@RequestBody DailyActivityGapControl dACGM){
//        DailyActivityGapControl approvedDailyActivityGapControl = dACGMService.approveDACGM(dACGM);
//        return new ResponseEntity<>(approvedDailyActivityGapControl, HttpStatus.CREATED);
//    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")

    public ResponseEntity<?> deleteDACGM(@PathVariable("id") Long id) {
        dACGMService.deleteDACGM(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @PatchMapping("/approveActionPlan/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_DISTRICT_IC')")
    public ResponseEntity<DailyActivityGapControl> approveActionPlan(@PathVariable Long id, @RequestBody Map<String, String> requestBody)
    {
        try
        {
            DailyActivityGapControl row = dACGMService.approveActionPlan(id,  requestBody.get("actionPlanDueDate"));
            return ResponseEntity.ok(row);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PatchMapping("/escalate/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER')")
    public ResponseEntity<DailyActivityGapControl>escalatePlan(@PathVariable("id") Long id)
    {
        try
        {
            DailyActivityGapControl row = dACGMService.escalatePlan(id);
            return ResponseEntity.ok(row);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

        }

    /*@GetMapping("/getFrequencyForAccountNumber/{accountNumber}")
    public Frequency getDCFrequency
            (@PathVariable("accountNumber") String accountNumber){
        int frequency  = dACGMService.findDACGMByAccountNumber(accountNumber);
        Frequency frequencyCount = new Frequency();
        frequencyCount.setCount(frequency);
        return frequencyCount;
    }*/
