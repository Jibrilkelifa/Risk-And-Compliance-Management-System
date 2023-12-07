package com.cbo.CBO_NFOS_ICMS.controllers;

import com.cbo.CBO_NFOS_ICMS.models.SubModule;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SubModule")
public class SubModuleController {
    private final SubModuleService subModuleService;

    public SubModuleController(SubModuleService subModuleService) {
        this.subModuleService = subModuleService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SubModule>> getAllSubModules() {
        List<SubModule> subModules = subModuleService.findAllSubModule();
        return new ResponseEntity<>(subModules, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<SubModule> getSubModuleId(@PathVariable("id") Long id) {
        SubModule subModule = subModuleService.findSubModuleById(id);
        return new ResponseEntity<>(subModule, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH')")
    public ResponseEntity<SubModule> addSubModule(@RequestBody SubModule subModule) {
        SubModule newSubModule = subModuleService.addSubModule(subModule);
        return new ResponseEntity<>(newSubModule, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<SubModule> updateSubModule(@RequestBody SubModule subModule) {
        SubModule updateSubModule = subModuleService.updateSubModule(subModule);
        return new ResponseEntity<>(updateSubModule, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteSubModule(@PathVariable("id") Long id) {
        subModuleService.deleteSubModule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
