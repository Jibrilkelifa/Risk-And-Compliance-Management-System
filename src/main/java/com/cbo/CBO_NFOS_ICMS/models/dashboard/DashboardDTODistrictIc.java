package com.cbo.CBO_NFOS_ICMS.models.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardDTOBranchIc {
    private int  OutstandingCases;
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
    private BigDecimal totalOutstandingCasesForDacgmDuringTheMonth;
    private  int NonFinancialOutstandingCases;
    private BigDecimal FinancialOutstandingCases;
    private BigDecimal financialChangePercentage;
    private BigDecimal nonFinancialChangePercentage;
    //rectified
    private BigDecimal totalRectifiedCasesForDacgmDuringTheMonth;
    private  int NonFinancialRectifiedCases;
    private BigDecimal FinancialRectifiedCases;
    private BigDecimal financialChangePercentager;
    private BigDecimal nonFinancialChangePercentager;
    //identified
    private BigDecimal totalIdentifiedCasesForDacgmDuringTheMonth;
    private  int NonFinancialIdentifiedCases;
    private BigDecimal FinancialIdentifiedCases;
    private BigDecimal financialChangePercentagei;
    private BigDecimal nonFinancialChangePercentagei;

    //todays cases
    private int newCasesToday;
    private int dueIn30Days;
    private int dueCases;
    private int outstandingEscalatedCases;



}
