package com.cbo.CBO_NFOS_ICMS.controllers.FireExtinguisherController;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher.FireExtinguisher;
import com.cbo.CBO_NFOS_ICMS.services.FireExtinguisherService.FireExtinguisherService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("FireExtinguisher")
public class FireExtinguisherController {
    private final FireExtinguisherService fireExtinguisherService;
    private final SubProcessService subProcessService;

    public FireExtinguisherController(FireExtinguisherService fireExtinguisherService, SubProcessService subProcessService) {
        this.fireExtinguisherService = fireExtinguisherService;
        this.subProcessService = subProcessService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<FireExtinguisher>> getFireExtinguisher() {
        List<FireExtinguisher> FireExtinguisher = fireExtinguisherService.findAllFireExtinguisher();
        return new ResponseEntity<>(FireExtinguisher, HttpStatus.OK);
    }
    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC')")
    public ResponseEntity<List<FireExtinguisher>> getAllFEInSpecificOrganizationalUnit(@PathVariable("id") String id) {
        List<FireExtinguisher> fire;
        fire = fireExtinguisherService.findAllFireExtinguisherBYBranch(id);
        return new ResponseEntity<>(fire, HttpStatus.OK);
    }

    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR')")
    public ResponseEntity<List<FireExtinguisher>> getAllFEInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<FireExtinguisher> fireExtinguisher;
        fireExtinguisher = fireExtinguisherService.findAllFireExtinguisherSubProcess(subProcessId);
        return new ResponseEntity<>(fireExtinguisher, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<FireExtinguisher> getFireExtinguisherId
            (@PathVariable("id") Long id) {
        FireExtinguisher fireExtinguisher = fireExtinguisherService.findFireExtinguisherById(id);
        return new ResponseEntity<>(fireExtinguisher, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<FireExtinguisher> addFireExtinguisher
            (@RequestBody FireExtinguisher fireExtinguisher) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);


        FireExtinguisher newfireExtinguisher = fireExtinguisherService.addFireExtinguisher(fireExtinguisher);
        return new ResponseEntity<>(newfireExtinguisher, HttpStatus.CREATED);
    }



    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<FireExtinguisher> updateFireExtinguisher
            (@RequestBody FireExtinguisher fireExtinguisher) {
        FireExtinguisher updateFireExtinguisher = fireExtinguisherService.updateFireExtinguisher(fireExtinguisher);
        return new ResponseEntity<>(updateFireExtinguisher, HttpStatus.CREATED);

    }

    @GetMapping("/getSize")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_DIRECTOR')")
    public int getFireExtinguisherSize() {
        return fireExtinguisherService.findFireExtinguisherSize();
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")

    public ResponseEntity<?> deleteFireExtinguisher(@PathVariable("id") Long id) {
        fireExtinguisherService.deleteFireExtinguisher(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
