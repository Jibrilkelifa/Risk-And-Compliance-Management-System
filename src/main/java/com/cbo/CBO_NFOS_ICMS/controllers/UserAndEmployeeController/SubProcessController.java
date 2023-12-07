package com.cbo.CBO_NFOS_ICMS.controllers.UserAndEmployeeController;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subProcess")
public class SubProcessController {

       private final SubProcessService subProcessService;

        public SubProcessController(SubProcessService subProcessService) {
            this.subProcessService = subProcessService;
        }
        @GetMapping("/{id}/allOrganizationalUnites")
        public ResponseEntity<List<Branch>> getAllOrganizationalUnites(@PathVariable("id") Long id){

            SubProcess subProcess = subProcessService.findSubProcessById (id);
            List<Branch> organizationalUnits = subProcessService.findAllBranch(subProcess);
            return new ResponseEntity<>(organizationalUnits, HttpStatus.OK);
        }
        @GetMapping("/all")
        public ResponseEntity<List<SubProcess>> getAllSubProcess(){
            List<SubProcess> subProcesss = subProcessService.findAllSubProcess();
            return new ResponseEntity<>(subProcesss, HttpStatus.OK);
        }
        @GetMapping("/find/{id}")
        public ResponseEntity<SubProcess> getSubProcessId (@PathVariable("id") Long id) {
            SubProcess subProcess= subProcessService.findSubProcessById(id);
            return new ResponseEntity<>(subProcess, HttpStatus.OK);
        }
        @PostMapping("/add")
        @PreAuthorize("hasRole('SUPER_ADMIN')")
        public ResponseEntity<SubProcess> addSubProcess(@RequestBody SubProcess subProcess) {
           SubProcess newSubProcess =subProcessService.addSubProcess(subProcess);
            return new ResponseEntity<>(newSubProcess, HttpStatus.CREATED);
        }
        @PutMapping("/update")
        @PreAuthorize("hasRole('SUPER_ADMIN')")
        public ResponseEntity<SubProcess> updateSubProcess(@RequestBody SubProcess subProcess) {
           SubProcess updateSubProcess =subProcessService.updateSubProcess(subProcess);
            return new ResponseEntity<>(updateSubProcess, HttpStatus.CREATED);
        }
        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasRole('SUPER_ADMIN')")
        public ResponseEntity<?> deleteSubProcess(@PathVariable("id") Long id) {
            subProcessService.deleteSubProcess(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        @GetMapping("/getAllDistrictList")
        public ResponseEntity<String[]> getAllDistricts() {
            String[] subProcesses = subProcessService.findAllDistricts();
            return new ResponseEntity<>(subProcesses, HttpStatus.OK);
        }
    }




