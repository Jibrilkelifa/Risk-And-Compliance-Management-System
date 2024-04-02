package com.cbo.CBO_NFOS_ICMS.controllers.dashboardController;

import com.cbo.CBO_NFOS_ICMS.models.dashboard.DashboardDTOBranchIc;
import com.cbo.CBO_NFOS_ICMS.services.dashboardService.DashboardBranchIcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view")
public class dashboard {
    private  final DashboardBranchIcService dashboardBranchIcService;

    public dashboard(DashboardBranchIcService dashboardBranchIcService) {
        this.dashboardBranchIcService = dashboardBranchIcService;
    }

    @GetMapping("/dashboard/{branchId}")
    public ResponseEntity<DashboardDTOBranchIc> getDashboardData(@PathVariable String branchId) {
        DashboardDTOBranchIc dashboardData = dashboardBranchIcService.getDashboardData(branchId);
        return new ResponseEntity<>(dashboardData, HttpStatus.OK);
    }
}
