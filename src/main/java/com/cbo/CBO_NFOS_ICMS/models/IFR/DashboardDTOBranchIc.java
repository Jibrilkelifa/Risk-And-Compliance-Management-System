package com.cbo.CBO_NFOS_ICMS.models.IFR;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardDTOBranchIc {
        private int totalOutstandingCases;
        private int newCasesDuringQuarter;
        private int closedCases;
        private BigDecimal outstandingCasesAmount;
        // Insurance policy related fields
        private int totalActivePolicies;
        private int expiringIn30Days;
        private int expiredPolicies;
        private BigDecimal estimatedLossAmount;
        // Fields related to FireExtinguisher module
        private int totalFireExtinguishers;
        private int expiringIn30DaysExtinguishers;
        private int expiredExtinguishers;
        private BigDecimal estimatedFineAmount;
        // Fields related to DailyActivityGapControl module
        private int identifiedCasesDuringMonth;
        private int rectifiedCasesDuringMonth;
        private int totalOutstandingCasesDacgm;

     //todays cases
        private int newCasesToday;
        private int dueIn30Days;
        private int dueCases;
        private int outstandingEscalatedCases;


}
