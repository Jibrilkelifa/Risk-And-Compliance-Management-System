package com.cbo.CBO_NFOS_ICMS.controllers.IFRController;

import com.cbo.CBO_NFOS_ICMS.models.IFR.CaseStatus;
import com.cbo.CBO_NFOS_ICMS.services.IFRService.CaseStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caseStatus")
public class CaseStatusController {
    private final CaseStatusService caseStatusService;

    public CaseStatusController(CaseStatusService caseStatusService) {
        this.caseStatusService = caseStatusService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CaseStatus>> getAllCaseStatuses() {
        List<CaseStatus> caseStatuses = caseStatusService.findAllCaseStatus();
        return new ResponseEntity<>(caseStatuses, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CaseStatus> getCaseStatusId(@PathVariable("id") Long id) {
        CaseStatus caseStatus = caseStatusService.findCaseStatusById(id);
        return new ResponseEntity<>(caseStatus, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<CaseStatus> addCaseStatus(@RequestBody CaseStatus caseStatus) {
        CaseStatus newCaseStatus = caseStatusService.addCaseStatus(caseStatus);
        return new ResponseEntity<>(newCaseStatus, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<CaseStatus> updateCaseStatus(@RequestBody CaseStatus caseStatus) {
        CaseStatus updateCaseStatus = caseStatusService.updateCaseStatus(caseStatus);
        return new ResponseEntity<>(updateCaseStatus, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteCaseStatus(@PathVariable("id") Long id) {
        caseStatusService.deleteCaseStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
