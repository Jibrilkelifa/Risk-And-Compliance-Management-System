package com.cbo.CBO_NFOS_ICMS.controllers.UserAndEmployeeController;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.OrganizationalUnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("organizationalUnit")
public class OrganizationalUnitController {

        private final OrganizationalUnitService organizationalUnitService;

        public OrganizationalUnitController(OrganizationalUnitService organizationalUnitService) {
            this.organizationalUnitService = organizationalUnitService;
        }
        @GetMapping("/all")
        @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
        public ResponseEntity<List<OrganizationalUnit>> getAllOrganizationalUnites(){
            List<OrganizationalUnit> organizationalUnits = organizationalUnitService.findAllOrganizationalUnit();
            return new ResponseEntity<>(organizationalUnits, HttpStatus.OK);
        }
        @GetMapping("/find/{id}")
        @PreAuthorize("hasAnyRole('ICMS_ADMIN','ICMS_BRANCH','ICMS_DISTRICT','ICMS_BRANCH_MANAGER')")
        public ResponseEntity<OrganizationalUnit> getOrganizationalUnitId (@PathVariable("id") Long id) {
           OrganizationalUnit organizationalUnit= organizationalUnitService.findOrganizationalUnitById(id);
            return new ResponseEntity<>(organizationalUnit, HttpStatus.OK);
        }
    }


