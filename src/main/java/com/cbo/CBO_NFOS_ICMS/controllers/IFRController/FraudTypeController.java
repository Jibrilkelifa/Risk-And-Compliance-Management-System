package com.cbo.CBO_NFOS_ICMS.controllers.IFRController;

import com.cbo.CBO_NFOS_ICMS.models.IFR.FraudType;
import com.cbo.CBO_NFOS_ICMS.services.IFRService.FraudTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/fraudType")
public class FraudTypeController {
    private final FraudTypeService fraudTypeService;
    public FraudTypeController(FraudTypeService fraudTypeService) {
        this.fraudTypeService = fraudTypeService;
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<FraudType>> getAllFraudTypes(){
        List<FraudType> fraudTypes = fraudTypeService.findAllFraudType();
        return new ResponseEntity<>(fraudTypes, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<FraudType> getFraudTypeId (@PathVariable("id") Long id) {
        FraudType fraudType= fraudTypeService.findFraudTypeById(id);
        return new ResponseEntity<>(fraudType, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<FraudType> addFraudType(@RequestBody FraudType fraudType) {
        FraudType newFraudType =fraudTypeService.addFraudType(fraudType);
        return new ResponseEntity<>(newFraudType, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<FraudType> updateFraudType(@RequestBody FraudType fraudType) {
        FraudType updateFraudType =fraudTypeService.updateFraudType(fraudType);
        return new ResponseEntity<>(updateFraudType, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<String> deleteFraudType(@PathVariable("id") Long id) {
        fraudTypeService.deleteFraudType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
