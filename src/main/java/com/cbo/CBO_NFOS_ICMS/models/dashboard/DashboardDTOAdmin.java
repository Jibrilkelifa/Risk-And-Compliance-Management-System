package com.cbo.CBO_NFOS_ICMS.models.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardDTOAdmin {
    private int  fraudOutstandingCases;
    private int fraudNewCasesDuringQuarter;
    private int fraudClosedCases;
    private BigDecimal fraudOutstandingCasesAmount;
    // Insurance policy related fields
    private int insuranceTotalActivePolicies;
    private int insuranceExpiringIn30Days;
    private int insuranceExpiredPolicies;
    private BigDecimal insuranceEstimatedLossAmount;
    // Fields related to FireExtinguisher module
    private int fireTotalFireExtinguishers;
    private int fireExpiringIn30DaysExtinguishers;
    private int fireExpiredExtinguishers;
    private BigDecimal fireEstimatedFineAmount;
    // Fields related to DailyActivityGapControl module
    private int dacgmIdentifiedCasesDuringMonth;
    private int dacgmRectifiedCasesDuringMonth;
    private BigDecimal dacgmTotalOutstandingCasesForDacgmDuringTheMonth;
    private  int NonFinancialOutstandingCases;
    private BigDecimal FinancialOutstandingCases;
    private BigDecimal financialChangePercentage;
    private BigDecimal nonFinancialChangePercentage;
    //rectified
    private BigDecimal dacgmTotalRectifiedCasesForDacgmDuringTheMonth;
    private  int NonFinancialRectifiedCases;
    private BigDecimal FinancialRectifiedCases;
    private BigDecimal financialChangePercentageForRectified;
    private BigDecimal nonFinancialChangePercentageForRectified;
    //identified
    private BigDecimal dacgmTotalIdentifiedCasesForDacgmDuringTheMonth;
    private  int NonFinancialIdentifiedCases;
    private BigDecimal FinancialIdentifiedCases;
    private BigDecimal financialChangePercentageForIdentified;
    private BigDecimal nonFinancialChangePercentageForidentified;

    //todays cases
    private int currentDaynewCasesToday;
    private int currentDaydueIn30Days;
    private int currentDaydueCases;
    private int currentDayoutstandingEscalatedCases;



}
