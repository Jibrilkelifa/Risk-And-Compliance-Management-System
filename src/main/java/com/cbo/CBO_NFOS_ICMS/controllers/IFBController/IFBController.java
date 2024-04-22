package com.cbo.CBO_NFOS_ICMS.controllers.IFBController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.IFB.IFB;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.CollateralInsurancePolicyService;
import com.cbo.CBO_NFOS_ICMS.services.IFBService.IFBService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("IFB")
public class IFBController {
    private final IFBService ifbService;
    private final SubProcessService subProcessService;

    public IFBController(IFBService ifbService, SubProcessService subProcessService) {
        this.ifbService = ifbService;
        this.subProcessService = subProcessService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_IFB','ICMS_ADMIN')")
    public ResponseEntity<List<IFB>> getIFB() {
        List<IFB> IFB = ifbService.findAllIFB();
        return new ResponseEntity<>(IFB, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<IFB> getIFBId
            (@PathVariable("id") Long id) {
        IFB IFB = ifbService.findIFBById(id);
        return new ResponseEntity<>(IFB, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ICMS_IFB')")
    public ResponseEntity<IFB> addIFB
            (@RequestBody IFB ifb) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);

        String caseId = ifb.getCaseId();
        while (ifbService.isCaseIdExists(caseId)) {
            // Increment the caseId until it is unique
            caseId = incrementCaseId(caseId);
        }
        ifb.setCaseId(caseId);
        System.out.println(caseId);

        IFB newIFB = ifbService.addIFB(ifb);
        return new ResponseEntity<>(newIFB, HttpStatus.CREATED);
    }

    private String incrementCaseId(String caseId) {
        String[] parts = caseId.split("/");
        int year = Integer.parseInt(parts[3]);
        int month = Integer.parseInt(parts[2]);
        int day = Integer.parseInt(parts[1]);
        int caseNumber = Integer.parseInt(parts[0]);

        // Increment the case number
        caseNumber++;

        // Reset the case number to 1 if the year has changed
        if (year > Integer.parseInt(parts[3])) {
            caseNumber = 1;
        }

        // Format the incremented values into the new caseId
        return String.format("%04d/%02d/%02d/%04d", caseNumber, day, month, year);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_IFB')")
    public ResponseEntity<IFB> updateIFB
            (@RequestBody IFB ifb) {
        System.out.println(ifb.getStatus());
        IFB updateIFB = ifbService.updateIFB(ifb);
        return new ResponseEntity<>(updateIFB, HttpStatus.CREATED);

    }

    @GetMapping("/getSize")
    @PreAuthorize("hasAnyRole('ICMS_IFB')")
    public int getIFBSize() {
        return ifbService.findIFBSize();
    }

    @PatchMapping("/authorize/{id}")
    @PreAuthorize("hasAnyRole('ICMS_IFB')")
    public ResponseEntity<IFB> updateTableRow(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        try {
            IFB row = ifbService.authorizeIFR(id, requestBody.get("authorizer"));
            return ResponseEntity.ok(row);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_IFB')")

    public ResponseEntity<?> deleteIFB(@PathVariable("id") Long id) {
        ifbService.deleteIFB(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
