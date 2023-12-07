package com.cbo.CBO_NFOS_ICMS.controllers.DCQController;

import com.cbo.CBO_NFOS_ICMS.models.DCQ.ChequeType;
import com.cbo.CBO_NFOS_ICMS.services.DCQService.ChequeTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chequeType")
public class ChequeTypeController {
    private final ChequeTypeService chequeTypeService;

    public ChequeTypeController(ChequeTypeService chequeTypeService) {
        this.chequeTypeService = chequeTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ChequeType>> getAllChequeTypes() {
        List<ChequeType> ChequeTypes = chequeTypeService.findAllChequeType();
        return new ResponseEntity<>(ChequeTypes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ChequeType> getChequeTypeId(@PathVariable("id") Long id) {
        ChequeType ChequeType = chequeTypeService.findChequeTypeById(id);
        return new ResponseEntity<>(ChequeType, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<ChequeType> addChequeType(@RequestBody ChequeType ChequeType) {
        ChequeType newChequeType = chequeTypeService.addChequeType(ChequeType);
        return new ResponseEntity<>(newChequeType, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<ChequeType> updateChequeType(@RequestBody ChequeType ChequeType) {
        ChequeType updateChequeType = chequeTypeService.updateChequeType(ChequeType);
        return new ResponseEntity<>(updateChequeType, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteChequeType(@PathVariable("id") Long id) {
        chequeTypeService.deleteChequeType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
