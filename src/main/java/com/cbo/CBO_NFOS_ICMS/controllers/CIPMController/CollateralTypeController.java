package com.cbo.CBO_NFOS_ICMS.controllers.CIPMController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralType;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.CollateralTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CollateralType")
public class CollateralTypeController {
    private final CollateralTypeService collateralTypeService;

    public CollateralTypeController(CollateralTypeService collateralTypeService) {
        this.collateralTypeService = collateralTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CollateralType>> getAllCollateralTypes() {
        List<CollateralType> collateralTypes = collateralTypeService.findAllCollateralType();
        return new ResponseEntity<>(collateralTypes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CollateralType> getCollateralTypeId(@PathVariable("id") Long id) {
        CollateralType collateralType = collateralTypeService.findCollateralTypeById(id);
        return new ResponseEntity<>(collateralType, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<CollateralType> addCollateralType(@RequestBody CollateralType collateralType) {
        CollateralType newCollateralType = collateralTypeService.addCollateralType(collateralType);
        return new ResponseEntity<>(newCollateralType, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<CollateralType> updateCollateralType(@RequestBody CollateralType collateralType) {
        CollateralType updateCollateralType = collateralTypeService.updateCollateralType(collateralType);
        return new ResponseEntity<>(updateCollateralType, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteCollateralType(@PathVariable("id") Long id) {
        collateralTypeService.deleteCollateralType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
