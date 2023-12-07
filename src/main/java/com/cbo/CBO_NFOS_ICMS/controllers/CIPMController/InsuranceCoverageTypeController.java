package com.cbo.CBO_NFOS_ICMS.controllers.CIPMController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.InsuranceCoverageType;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.InsuranceCoverageTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insuranceCoverageType")
public class InsuranceCoverageTypeController {
    private final InsuranceCoverageTypeService insuranceCoverageTypeService;

    public InsuranceCoverageTypeController(InsuranceCoverageTypeService insuranceCoverageTypeService) {
        this.insuranceCoverageTypeService = insuranceCoverageTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<InsuranceCoverageType>> getAllInsuranceCoverageTypes() {
        List<InsuranceCoverageType> insuranceCoverageTypes = insuranceCoverageTypeService.findAllInsuranceCoverageType();
        return new ResponseEntity<>(insuranceCoverageTypes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<InsuranceCoverageType> getInsuranceCoverageTypeId(@PathVariable("id") Long id) {
        InsuranceCoverageType insuranceCoverageType = insuranceCoverageTypeService.findInsuranceCoverageTypeById(id);
        return new ResponseEntity<>(insuranceCoverageType, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<InsuranceCoverageType> addInsuranceCoverageType(@RequestBody InsuranceCoverageType insuranceCoverageType) {
        InsuranceCoverageType newinsuranceCoverageType = insuranceCoverageTypeService.addInsuranceCoverageType(insuranceCoverageType);
        return new ResponseEntity<>(insuranceCoverageType, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<InsuranceCoverageType> updateInsuranceCoverageType(@RequestBody InsuranceCoverageType insuranceCoverageType) {
        InsuranceCoverageType updateInsuranceCoverageType = insuranceCoverageTypeService.updateInsuranceCoverageType(insuranceCoverageType);
        return new ResponseEntity<>(updateInsuranceCoverageType, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteCollateralType(@PathVariable("id") Long id) {
        insuranceCoverageTypeService.deleteInsuranceCoverageType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
