package com.cbo.CBO_NFOS_ICMS.controllers.FinanceController;


import com.cbo.CBO_NFOS_ICMS.models.Finance.FinanceStatus;
import com.cbo.CBO_NFOS_ICMS.services.FinanceService.FinanceStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/FinanceStatus")
public class FinanceStatusController {
    private final FinanceStatusService financeStatusService;

    public FinanceStatusController(FinanceStatusService financeStatusService) {
        this.financeStatusService = financeStatusService;
    }

    @GetMapping("/getAll")

    public ResponseEntity<List<FinanceStatus>> getAllFinanceStatus() {
        List<FinanceStatus> activitiesStatus = financeStatusService.findAllFinanceStatus();
        return new ResponseEntity<>(activitiesStatus, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<FinanceStatus> getAllDACGMInSpecificOrganizationalUnit(@PathVariable("id") Long id) {
        FinanceStatus financeStatus = financeStatusService.findFinanceStatusById(id);
        return new ResponseEntity<>(financeStatus, HttpStatus.OK);
    }

}