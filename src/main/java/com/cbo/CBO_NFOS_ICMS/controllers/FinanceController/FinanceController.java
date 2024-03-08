package com.cbo.CBO_NFOS_ICMS.controllers.FinanceController;

import com.cbo.CBO_NFOS_ICMS.models.Finance.Finance;
import com.cbo.CBO_NFOS_ICMS.models.IFB.IFB;
import com.cbo.CBO_NFOS_ICMS.services.FinanceService.FinanceService;
import com.cbo.CBO_NFOS_ICMS.services.IFBService.IFBService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Finance")
public class FinanceController {
    private final FinanceService financeService;
    private final SubProcessService subProcessService;

    public FinanceController(FinanceService financeService, SubProcessService subProcessService) {
        this.financeService = financeService;
        this.subProcessService = subProcessService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_IC')")
    public ResponseEntity<List<Finance>> getIFB() {
        List<Finance> Finance = financeService.findAllFinance();
        return new ResponseEntity<>(Finance, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Finance> getIFBId
            (@PathVariable("id") Long id) {
        Finance Finance = financeService.findFinanceById(id);
        return new ResponseEntity<>(Finance, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<Finance> addIFB
            (@RequestBody Finance finance) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String formattedDate = date.format(formatter);

        String caseId = finance.getCaseId();
        while (financeService.isCaseIdExists(caseId)) {
            // Increment the caseId until it is unique
            caseId = incrementCaseId(caseId);
        }
        finance.setCaseId(caseId);
        System.out.println(caseId);

        Finance newfinance = financeService.addIFB(finance);
        return new ResponseEntity<>(newfinance, HttpStatus.CREATED);
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
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")
    public ResponseEntity<Finance> updateIFB
            (@RequestBody Finance finance) {
        System.out.println(finance.getStatus());
        Finance updateFinance = financeService.updateFinance(finance);
        return new ResponseEntity<>(updateFinance, HttpStatus.CREATED);

    }

    @GetMapping("/getSize")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_BRANCH_IC', 'ICMS_PROVISION','ICMS_ADMIN','ICMS_DISTRICT_DIRECTOR')")
    public int getIFBSize() {
        return financeService.findFinanceSize();
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")

    public ResponseEntity<?> deleteIFB(@PathVariable("id") Long id) {
        financeService.deleteFinance(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}