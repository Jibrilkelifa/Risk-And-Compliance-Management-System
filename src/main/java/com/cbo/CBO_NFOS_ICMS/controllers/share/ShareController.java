package com.cbo.CBO_NFOS_ICMS.controllers.share;


import com.cbo.CBO_NFOS_ICMS.models.share.Share;
import com.cbo.CBO_NFOS_ICMS.services.share.ShareService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Share")
public class ShareController {
    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<Share>> getAllShare() {
        List<Share> share = shareService.findAllShare();
        return new ResponseEntity<>(share, HttpStatus.OK);
    }


    @GetMapping("/getSize")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC', 'ICMS_PROVISION')")
    public int  getShareSize(){

        return shareService.findShareSize();
    }

    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC')")
    public ResponseEntity<List<Share>> getAllShareInSpecificOrganizationalUnit(@PathVariable("id") Long id) {
        List<Share> share;
        share = shareService.findAllShareInSpecificOrganizationalUnit(id);
        return new ResponseEntity<>(share, HttpStatus.OK);
    }

    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR')")
    public ResponseEntity<List<Share>> getAllDailyActivityGapInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<Share> Share;
        Share = shareService.findAllShareInSpecificSubProcess(subProcessId);
        return new ResponseEntity<>(Share, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Share> getShareId
            (@PathVariable("id") Long id) {
        Share share = shareService.findShareById(id);
        return new ResponseEntity<>(share, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<Share> addShare
            (@RequestBody Share share) {
        String caseId = share.getCaseId();
        while (shareService.isCaseIdExists(caseId)) {
            // Increment the caseId until it is unique
            caseId = incrementCaseId(caseId);
        }
        share.setCaseId(caseId);
        System.out.println(caseId);
        Share newShare = shareService.addShare(share);
        return new ResponseEntity<>(newShare, HttpStatus.CREATED);
    }
    private String incrementCaseId(String caseId) {
        String[] parts = caseId.split("/");
//<<<<<<< HEAD
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
//=======
//        int year = Integer.parseInt(parts[2]);
//        int month = Integer.parseInt(parts[1]);
//        int day = Integer.parseInt(parts[0]);
//
//        // Increment the day, month, or year as needed
//        // Here we assume a simple increment, but you can implement your own logic based on your requirements
//        if (day < 31) {
//            day++;
//        } else {
//            day = 1;
//            if (month < 12) {
//                month++;
//            } else {
//                month = 1;
//                year++;
//            }
//        }
//
//        // Reset the caseId to "001" if the year has changed
//        if (year > Integer.parseInt(parts[2])) {
//            return "001/01/01/" + String.format("%04d", year);
//        }
//
//        // Format the incremented values into the new caseId
//        return String.format("%03d/%02d/%02d/%04d", day, month, year);
//>>>>>>> daee81c8c31a4d330f9fb199b891a66bfdcb146d
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<Share> updateShare
            (@RequestBody Share share) {
        Share updateShare = shareService.updateShare(share);
        return new ResponseEntity<>(updateShare, HttpStatus.CREATED);
    }

    //    @PutMapping("/approve/{id}")
//    @PreAuthorize("hasRole('ICMS_BRANCH_MANAGER')")
//    public ResponseEntity<Share> approveShare
//            (@RequestBody Share share){
//        Share approvedShare = shareService.approveShare(share);
//        return new ResponseEntity<>(approvedShare, HttpStatus.CREATED);
//    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")

    public ResponseEntity<?> deleteShare(@PathVariable("id") Long id) {
        shareService.deleteShare(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

    /*@GetMapping("/getFrequencyForAccountNumber/{accountNumber}")
    public Frequency getDCFrequency
            (@PathVariable("accountNumber") String accountNumber){
        int frequency  = shareService.findShareByAccountNumber(accountNumber);
        Frequency frequencyCount = new Frequency();
        frequencyCount.setCount(frequency);
        return frequencyCount;
    }*/
