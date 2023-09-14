package com.cbo.CBO_NFOS_ICMS.controllers.CIPMController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.SuspectedFraudsterProfession;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.SuspectedFraudsterProfessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/suspectedFraudsterProfession")
public class SuspectedFraudsterProfessionController {
    private final SuspectedFraudsterProfessionService suspectedFraudsterProfessionService;

    public SuspectedFraudsterProfessionController(SuspectedFraudsterProfessionService suspectedFraudsterProfessionService) {
        this.suspectedFraudsterProfessionService = suspectedFraudsterProfessionService;
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<SuspectedFraudsterProfession>> getAllSuspectedFraudsterProfessiones(){
        List<SuspectedFraudsterProfession> suspectedFraudsterProfessiones =suspectedFraudsterProfessionService.findAllSuspectedFraudsterProfession();
        return new ResponseEntity<>(suspectedFraudsterProfessiones, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<SuspectedFraudsterProfession> getSuspectedFraudsterProfessionId (@PathVariable("id") Long id) {
        SuspectedFraudsterProfession SuspectedFraudsterProfession = suspectedFraudsterProfessionService.findSuspectedFraudsterProfessionById(id);
        return new ResponseEntity<>(SuspectedFraudsterProfession, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<SuspectedFraudsterProfession> addSuspectedFraudsterProfession(@RequestBody SuspectedFraudsterProfession SuspectedFraudsterProfession) {
        SuspectedFraudsterProfession newSuspectedFraudsterProfession =suspectedFraudsterProfessionService.addSuspectedFraudsterProfession(SuspectedFraudsterProfession);
        return new ResponseEntity<>(newSuspectedFraudsterProfession, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<SuspectedFraudsterProfession> updateSuspectedFraudsterProfession(@RequestBody SuspectedFraudsterProfession SuspectedFraudsterProfession) {
        SuspectedFraudsterProfession updateSuspectedFraudsterProfession =suspectedFraudsterProfessionService.updateSuspectedFraudsterProfession(SuspectedFraudsterProfession);
        return new ResponseEntity<>(updateSuspectedFraudsterProfession, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteSuspectedFraudsterProfession(@PathVariable("id") Long id) {
        suspectedFraudsterProfessionService.deleteSuspectedFraudsterProfession(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
