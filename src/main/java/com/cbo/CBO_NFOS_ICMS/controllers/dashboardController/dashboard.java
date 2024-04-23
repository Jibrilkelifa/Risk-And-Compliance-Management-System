package com.cbo.CBO_NFOS_ICMS.controllers.dashboardController;

import com.cbo.CBO_NFOS_ICMS.models.dashboard.DashboardDTOBranchIc;
import com.cbo.CBO_NFOS_ICMS.models.dashboard.DashboardDTODistrictIc;
import com.cbo.CBO_NFOS_ICMS.services.dashboardService.DashboardBranchIcService;
import com.cbo.CBO_NFOS_ICMS.services.dashboardService.DashboardDistrictIcService;
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
    private final DashboardDistrictIcService dashboardDistrictIcService;

    public dashboard(DashboardBranchIcService dashboardBranchIcService, DashboardDistrictIcService dashboardDistrictIcService) {
        this.dashboardBranchIcService = dashboardBranchIcService;
        this.dashboardDistrictIcService = dashboardDistrictIcService;
    }

    @GetMapping("/dashboard/forBranchIc/{branchId}")
    public ResponseEntity<DashboardDTOBranchIc> getDashboardData(@PathVariable String branchId) {
        DashboardDTOBranchIc dashboardData = dashboardBranchIcService.getDashboardData(branchId);
        return new ResponseEntity<>(dashboardData, HttpStatus.OK);
    }
    @GetMapping("/dashboard/forDistrictIc/{subProcessId}")
    public ResponseEntity<DashboardDTODistrictIc> getDashboardDataForDistrictIc(@PathVariable Long subProcessId) {
        DashboardDTODistrictIc dashboardData = dashboardDistrictIcService.getDashboardData(subProcessId);
        return new ResponseEntity<>(dashboardData, HttpStatus.OK);
    }
}
