package com.cbo.CBO_NFOS_ICMS.controllers.UserAndEmployeeController;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("organizationalUnit")
public class OrganizationalUnitController {

        private final BranchService organizationalUnitService;

        public OrganizationalUnitController(BranchService organizationalUnitService) {
            this.organizationalUnitService = organizationalUnitService;
        }
        @GetMapping("/all")
        @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
        public ResponseEntity<List<Branch>> getAllOrganizationalUnites(){
            List<Branch> organizationalUnits = organizationalUnitService.findAllBranch();
            return new ResponseEntity<>(organizationalUnits, HttpStatus.OK);
        }
        @GetMapping("/find/{id}")
        @PreAuthorize("hasAnyRole('ICMS_ADMIN','ICMS_BRANCH','ICMS_DISTRICT','ICMS_BRANCH_MANAGER')")
        public ResponseEntity<Branch> getOrganizationalUnitId (@PathVariable("id") Long id) {
           Branch organizationalUnit= organizationalUnitService.findBranchById(id);
            return new ResponseEntity<>(organizationalUnit, HttpStatus.OK);
        }
    }


