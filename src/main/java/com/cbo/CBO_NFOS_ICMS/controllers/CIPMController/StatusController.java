package com.cbo.CBO_NFOS_ICMS.controllers.CIPMController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
import com.cbo.CBO_NFOS_ICMS.models.IFR.CaseStatus;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.StatusService;
import com.cbo.CBO_NFOS_ICMS.services.IFRService.CaseStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Status")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Status>> getAllStatuses(){
        List<Status> Statuses =statusService.findAllStatus();
        return new ResponseEntity<>(Statuses, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<Status> getStatusId (@PathVariable("id") Long id) {
        Status caseStatus= statusService.findStatusById(id);
        return new ResponseEntity<>(caseStatus, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<Status> addCaseStatus(@RequestBody Status status) {
        Status statuse =statusService.addStatus(status);
        return new ResponseEntity<>(statuse, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<Status> updateCaseStatus(@RequestBody Status status) {
        Status updateStatus=statusService.updateStatus(status);
        return new ResponseEntity<>(updateStatus, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteStatus(@PathVariable("id") Long id) {
        statusService.deleteStatus(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
