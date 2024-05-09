package com.cbo.CBO_NFOS_ICMS.services.dashboardService;

import com.cbo.CBO_NFOS_ICMS.models.AllIrregularity;
import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher.FireExtinguisher;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.dashboard.DashboardDTOBranchIc;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.CollateralInsurancePolicyRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository.DailyActivityGapControlRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.FireExtinguisherRepository.FireExtinguisherRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository.IncidentOrFraudRepository;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.CollateralInsurancePolicyService;
import com.cbo.CBO_NFOS_ICMS.services.DACGMService.DailyActivityGapControlService;
import com.cbo.CBO_NFOS_ICMS.services.FireExtinguisherService.FireExtinguisherService;
import com.cbo.CBO_NFOS_ICMS.services.IFRService.IncidentOrFraudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardBranchIcService {
    @Autowired
    private IncidentOrFraudService incidentOrFraudService;
    @Autowired
    private DailyActivityGapControlService dailyActivityGapControlService;
    @Autowired
    private DailyActivityGapControlRepository dailyActivityGapControlRepository;
    @Autowired
    private IncidentOrFraudRepository incidentOrFraudRepository;
    @Autowired
    private CollateralInsurancePolicyService collateralInsurancePolicyService;
    @Autowired
    private FireExtinguisherRepository fireExtinguisherRepository;
    @Autowired
    private FireExtinguisherService fireExtinguisherService;
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    @Autowired
    private CollateralInsurancePolicyRepository collateralInsurancePolicyRepository;

    public DashboardDTOBranchIc getDashboardData(String branchId) {

        int totalOutstandingCases = calculateTotalOutstandingCases(branchId);
        Object[] newCases = calculateNewCasesDuringQuarter(branchId);
        int newCasesToday = (int) newCases[0];
        int dueIn30Days = (int) newCases[1];
        int dueCases = (int) newCases[2];
        int outstandingEscalatedCases =(int) newCases[3];

        int closedCases = calculateClosedCases(branchId);
        BigDecimal outstandingCasesAmount = calculateOutstandingCasesAmount(branchId);
        // Calculate insurance policy-related data
        int totalActivePolicies = getActivePoliciesByBranch(branchId);
        int expiringIn30Days = calculateExpiringIn30Days(branchId);
        int expiredPolicies = calculateExpiredPolicies(branchId);
        BigDecimal estimatedLossAmount = calculateEstimatedLossAmount(branchId);
        // Calculate results for FireExtinguisher module
        int totalFireExtinguishers = calculateTotalFireExtinguishers(branchId);
        int eexpiringIn30Days = calculateEexpiringIn30DaysExtinguiser(branchId);
        int expired = calculateExpiredFireExtinguishers(branchId);
        BigDecimal estimatedFineAmount = calculateEstimatedFineAmount(branchId);
        // Calculate results for DailyActivityGapControl module

//        BigDecimal outstandingCasesDuringMonth = calculateOutstandingCasesDuringMonth(branchId);

        Object[] result = calculateOutstandingCasesForBranch(branchId);
        BigDecimal totalFinancialOutstandingCases = (BigDecimal) result[0];
        int totalNonFinancialOutstandingCases = (int) result[1];
        BigDecimal outstandingCasesDuringMonth = (BigDecimal) result[2];
        BigDecimal financialPercentageFromLastOutstanding = (BigDecimal) result[3];
        BigDecimal nonFinancialPercentageFromLastOutstanding = (BigDecimal) result[4];
        Object[] resultt = calculateRectifiedCasesForBranch(branchId);
        BigDecimal totalFinancialRectifiedCases = (BigDecimal) resultt[0];
        int totalNonFinancialRectifiedCases = (int) resultt[1];
        BigDecimal rectifiedCasesDuringMonth = (BigDecimal) resultt[2];
        BigDecimal financialPercentageFromLastRectified = (BigDecimal) resultt[3];
        BigDecimal nonFinancialPercentageFromLastRectified = (BigDecimal) resultt[4];

        Object[] resulttt = calculateIdentifiedCasesForBranch(branchId);
        BigDecimal totalFinancialIdentifiedCases = (BigDecimal) resulttt[0];
        int totalNonFinancialIdentifiedCases = (int) resulttt[1];
        BigDecimal identifiedCasesDuringMonth = (BigDecimal) resulttt[2];
        BigDecimal financialPercentageFromLastIdentified = (BigDecimal) resulttt[3];
        BigDecimal nonFinancialPercentageFromLastIdentified = (BigDecimal) resulttt[4];


        // Populate the DashboardDTO
        //fraud
        DashboardDTOBranchIc dashboardDTO = new DashboardDTOBranchIc();
        dashboardDTO.setFraudOutstandingCases(totalOutstandingCases);
        dashboardDTO.setFraudClosedCases(closedCases);
        dashboardDTO.setFraudOutstandingCasesAmount(outstandingCasesAmount);
        // Populate CIPMs
        dashboardDTO.setInsuranceTotalActivePolicies(totalActivePolicies);
        dashboardDTO.setInsuranceExpiringIn30Days(expiringIn30Days);
        dashboardDTO.setInsuranceExpiredPolicies(expiredPolicies);
        dashboardDTO.setInsuranceEstimatedLossAmount(estimatedLossAmount);
        // for fire
        dashboardDTO.setFireTotalFireExtinguishers(totalFireExtinguishers);
        dashboardDTO.setFireExpiringIn30DaysExtinguishers(eexpiringIn30Days);
        dashboardDTO.setFireExpiredExtinguishers(expired);
        dashboardDTO.setFireEstimatedFineAmount(estimatedFineAmount);
        // for DACGM
        dashboardDTO.setDacgmTotalOutstandingCasesForDacgmDuringTheMonth(outstandingCasesDuringMonth);
        dashboardDTO.setFinancialOutstandingCases(totalFinancialOutstandingCases);
        dashboardDTO.setNonFinancialOutstandingCases(totalNonFinancialOutstandingCases);
        dashboardDTO.setFinancialChangePercentage(financialPercentageFromLastOutstanding);
        dashboardDTO.setNonFinancialChangePercentage(nonFinancialPercentageFromLastOutstanding);
        dashboardDTO.setDacgmTotalRectifiedCasesForDacgmDuringTheMonth(rectifiedCasesDuringMonth);
        dashboardDTO.setFinancialRectifiedCases(totalFinancialRectifiedCases);
        dashboardDTO.setNonFinancialRectifiedCases(totalNonFinancialRectifiedCases);
        dashboardDTO.setFinancialChangePercentageForRectified(financialPercentageFromLastRectified);
        dashboardDTO.setNonFinancialChangePercentageForRectified(nonFinancialPercentageFromLastRectified);
        dashboardDTO.setDacgmTotalIdentifiedCasesForDacgmDuringTheMonth(identifiedCasesDuringMonth);
        dashboardDTO.setFinancialIdentifiedCases(totalFinancialIdentifiedCases);
        dashboardDTO.setNonFinancialIdentifiedCases(totalNonFinancialIdentifiedCases);
        dashboardDTO.setFinancialChangePercentageForIdentified(financialPercentageFromLastIdentified);
        dashboardDTO.setNonFinancialChangePercentageForidentified(nonFinancialPercentageFromLastIdentified);
        dashboardDTO.setCurrentDaynewCasesToday(newCasesToday);
        dashboardDTO.setCurrentDayoutstandingEscalatedCases(outstandingEscalatedCases);
        dashboardDTO.setCurrentDaydueCases(dueCases);
        dashboardDTO.setCurrentDaydueIn30Days(dueIn30Days);

//        dashboardDTO.setNewCasesToday(newCasesToday);
//        dashboardDTO.setDueIn30Days(dueIn30Days);
//        dashboardDTO.setDueCases(dueCases);
//        dashboardDTO.setOutstandingEscalatedCases(outstandingEscalatedCases);

        return dashboardDTO;
    }

    public Object[] calculateOutstandingCasesForBranch(String branchId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);
        List<DailyActivityGapControl> controlss = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        // Filter controls based on the current month
        controls.removeIf(control -> {
            LocalDate controlDate = LocalDate.parse(control.getDate(),formatter);
            return controlDate.getMonthValue() != LocalDate.now().getMonthValue();
        });

        // Calculate financial and non-financial outstanding cases
        int  totalFinancialOutstandingCasess=0;
        BigDecimal totalFinancialOutstandingCases = BigDecimal.ZERO;
        int totalNonFinancialOutstandingCases = 0;
        BigDecimal tottalOutstandingCases = BigDecimal.ZERO;
        for (DailyActivityGapControl control : controls) {
            if (control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                if (isFinancialCategory(control)) {
                    totalFinancialOutstandingCasess++;
                    totalFinancialOutstandingCases = totalFinancialOutstandingCases.add(getAmountInvolved(control));
                } else {
                    totalNonFinancialOutstandingCases++;
                }
                tottalOutstandingCases = tottalOutstandingCases.add(BigDecimal.ONE);
            }
        }


        // Calculate the percentage change from last month for financial and non-financial cases
        BigDecimal financialChangePercentage = calculateChangePercentage(getTotalFinancialOutstandingCasesFromLastMonth(controlss), BigDecimal.valueOf(totalFinancialOutstandingCasess));
        BigDecimal nonFinancialChangePercentage = calculateChangePercentage(getTotalNonFinancialOutstandingCasesFromLastMonth(controlss), BigDecimal.valueOf(totalNonFinancialOutstandingCases));


        // Return all four values as an array
        return new Object[] {
                totalFinancialOutstandingCases,
                totalNonFinancialOutstandingCases,
                tottalOutstandingCases,
                financialChangePercentage,
                nonFinancialChangePercentage,
                totalFinancialOutstandingCasess



        };
    }

    private BigDecimal calculateChangePercentage(BigDecimal previousValue, BigDecimal currentValue) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {

            return BigDecimal.ZERO;
        }
        // Calculate percentage change: ((currentValue - previousValue) / previousValue) * 100
        return currentValue.subtract(previousValue).divide(previousValue, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    private boolean isFinancialCategory(DailyActivityGapControl control) {
        AllIrregularity irregularity = control.getIrregularity();
        if (irregularity == null) {
            return false; // Handle case where irregularity is null
        }
        AllSubCategory subCategory = irregularity.getAllSubCategory();
        // Check if the subcategory is of the "Financial" category
        return subCategory.getName().equals("Financial");
    }






    private BigDecimal getAmountInvolved(DailyActivityGapControl control) {
        String amountInvolved = control.getAmountInvolved();
        if (amountInvolved != null && !amountInvolved.isEmpty()) {
            try {
                return new BigDecimal(amountInvolved);
            } catch (NumberFormatException e) {
                // Log the error or handle it as needed
                e.printStackTrace();
            }
        }
        return BigDecimal.ZERO;
    }

    // Placeholder methods to get total financial and non-financial outstanding cases from last month
    private BigDecimal getTotalFinancialOutstandingCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int totalFinancialOutstandingCases = 0;
        for (DailyActivityGapControl control : controls) {
            // Parse the date string
            LocalDate controlDate = LocalDate.parse(control.getDate(),formatter);


            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                    totalFinancialOutstandingCases++;
                }
            }
        }
        return BigDecimal.valueOf(totalFinancialOutstandingCases);
    }


    private BigDecimal getTotalNonFinancialOutstandingCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        int totalNonFinancialOutstandingCases = 0;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(),formatter);

            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {

                if (!isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                    totalNonFinancialOutstandingCases++;
                }
            }
        }

        return BigDecimal.valueOf(totalNonFinancialOutstandingCases);
    }

///// for rectified cases

    public Object[] calculateRectifiedCasesForBranch(String branchId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controlss = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);

        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        // Filter controls based on the current month
        controls.removeIf(controll -> {
            LocalDate controlDate = LocalDate.parse(controll.getDate(),formatter);
            return controlDate.getMonthValue() != LocalDate.now().getMonthValue();
        });

        // Calculate financial and non-financial outstanding cases
        BigDecimal totalFinancialRectifiedCases = BigDecimal.ZERO;
        int totalFinancialRectifiedCasess=0;
        int totalNonFinancialRectifiedCases = 0;
        BigDecimal totalRectifiedCases = BigDecimal.ZERO;
        for (DailyActivityGapControl controll : controls) {
            if (controll.getActivityStatus() != null && controll.getActivityStatus().getName().equals("Closed")) {
                if (isFinancialCategory(controll)) {
                    totalFinancialRectifiedCases = totalFinancialRectifiedCases.add(getAmountInvolved(controll));

                    totalFinancialRectifiedCasess++;
                } else {
                    totalNonFinancialRectifiedCases++;
                }
                totalRectifiedCases = totalRectifiedCases.add(BigDecimal.ONE);
            }
        }

        // Calculate the percentage change from last month for financial and non-financial cases
        BigDecimal financialChangePercentageR = calculateChangePercentageR(BigDecimal.valueOf(getTotalFinancialRectifiedCasesFromLastMonth(controlss)), BigDecimal.valueOf(totalFinancialRectifiedCasess));
        BigDecimal nonFinancialChangePercentageR = calculateChangePercentageR(getTotalNonFinancialRectifiedasesFromLastMonth(controlss), BigDecimal.valueOf(totalNonFinancialRectifiedCases));

        // Return all four values as an array
        return new Object[] {
                totalFinancialRectifiedCases,
                totalNonFinancialRectifiedCases,
                totalRectifiedCases,
                financialChangePercentageR,
                nonFinancialChangePercentageR,
                totalFinancialRectifiedCasess

        };
    }

    private BigDecimal calculateChangePercentageR(BigDecimal previousValue, BigDecimal currentValue) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {

            return BigDecimal.ZERO;
        }
        // Calculate percentage change: ((currentValue - previousValue) / previousValue) * 100
        return currentValue.subtract(previousValue).divide(previousValue, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    // Placeholder methods to get total financial and non-financial outstanding cases from last month
    private int getTotalFinancialRectifiedCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int totalFinancialOutstandingCases = 0;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                    totalFinancialOutstandingCases++;
//                    totalFinancialOutstandingCases = totalFinancialOutstandingCases.add(getAmountInvolved(control));
                }
            }
        }
        return totalFinancialOutstandingCases;
    }

    private BigDecimal getTotalNonFinancialRectifiedasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int totalNonFinancialRectifiedCases = 0;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (!isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Closed")) {
                    totalNonFinancialRectifiedCases++;
                }
            }
        }

        return BigDecimal.valueOf(totalNonFinancialRectifiedCases);
    }


    public Object[] calculateIdentifiedCasesForBranch(String branchId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controlss = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);

        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        // Filter controls based on the current month
        controls.removeIf(control -> {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            return controlDate.getMonthValue() != LocalDate.now().getMonthValue();
        });

        // Calculate financial and non-financial identified cases
        int totalFinancialIdentifiedCasess = 0;
        BigDecimal totalFinancialIdentifiedCases = BigDecimal.ZERO;
        int totalNonFinancialIdentifiedCases = 0;
        BigDecimal totalIdentifiedCases = BigDecimal.ZERO;
        int totalNonFinancialIdentifiedCasess=0;
        for (DailyActivityGapControl control : controls) {
            if (control.getActivityStatus() != null) {
                if (isFinancialCategory(control)) {
                    totalFinancialIdentifiedCasess++;
                    totalFinancialIdentifiedCases = totalFinancialIdentifiedCases.add(getAmountInvolved(control));
                } else {
                    totalNonFinancialIdentifiedCases++;
                }
                totalIdentifiedCases = totalIdentifiedCases.add(BigDecimal.ONE);
            }
        }
        // Calculate the percentage change from last month for financial and non-financial cases
        BigDecimal financialChangePercentageI = calculateChangePercentageI(BigDecimal.valueOf(getTotalFinancialIdentifiedCasesFromLastMonth(controlss)), BigDecimal.valueOf(totalFinancialIdentifiedCasess));
        BigDecimal nonFinancialChangePercentageI = calculateChangePercentageI(getTotalNonFinancialIdentifiedCasesFromLastMonth(controlss), BigDecimal.valueOf(totalNonFinancialIdentifiedCases));

        // Return all four values as an array
        return new Object[] {
                totalFinancialIdentifiedCases,
                totalNonFinancialIdentifiedCases,
                totalIdentifiedCases,
                financialChangePercentageI,
                nonFinancialChangePercentageI,
                totalFinancialIdentifiedCasess
        };
    }

    private BigDecimal calculateChangePercentageI(BigDecimal previousValue, BigDecimal currentValue) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        // Calculate percentage change: ((currentValue - previousValue) / previousValue) * 100
        return currentValue.subtract(previousValue).divide(previousValue, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    // Placeholder methods to get total financial and non-financial identified cases from last month
    private int getTotalFinancialIdentifiedCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int totalFinancialIdentifiedCases = 0;

        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (isFinancialCategory(control) && control.getActivityStatus() != null) {
                    totalFinancialIdentifiedCases++;
//                    totalFinancialIdentifiedCases = totalFinancialIdentifiedCases.add(getAmountInvolved(control));
                }
            }
        }

        return totalFinancialIdentifiedCases;
    }

    private BigDecimal getTotalNonFinancialIdentifiedCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int totalNonFinancialIdentifiedCases = 0;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (!isFinancialCategory(control) && control.getActivityStatus() != null) {
                    totalNonFinancialIdentifiedCases++;
                }
            }
        }
        return BigDecimal.valueOf(totalNonFinancialIdentifiedCases);
    }






    public BigDecimal calculateEstimatedFineAmount(String branchId) {
        // Fetch the list of fire extinguishers for the branch
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);

        // Calculate the total fine amount
        int totalExpiredFires = calculateeExpiredFireExtinguishers(branchId);
        BigDecimal totalFineAmount = BigDecimal.valueOf(totalExpiredFires * 10000);

        return totalFineAmount;
    }

    private int calculateeExpiredFireExtinguishers(String branchId) {
        // Fetch the list of fire extinguishers for the branch
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);

        // Count the number of expired fire extinguishers
        int expiredCount = 0;
        for (FireExtinguisher fire : fires) {
            // Assuming the expiry date is stored in a field called 'inspectionDate'
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            LocalDate expiryDate = LocalDate.parse(fire.getInspectionDate(), formatter);
            if (expiryDate.isBefore(LocalDate.now())) {
                expiredCount++;
            }
        }

        return expiredCount;
    }

    private int calculateTotalFireExtinguishers(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
        return fires.size();
    }

    public BigDecimal calculateEstimatedLossAmount(String branchId) {
        // Fetch the list of expired policies for the branch
        List<CollateralInsurancePolicy> expiredPolicies = findExpiredPoliciesByBranchId(branchId);

        // Calculate the total sum insured for all expired policies
        BigDecimal totalLossAmount = BigDecimal.ZERO;
        for (CollateralInsurancePolicy policy : expiredPolicies) {
            BigDecimal policySumInsured = new BigDecimal(policy.getSumInsured());
            totalLossAmount = totalLossAmount.add(policySumInsured);
        }

        return totalLossAmount;
    }

    private List<CollateralInsurancePolicy> findExpiredPoliciesByBranchId(String branchId) {
        // Fetch the list of all policies for the branch
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);

        // Create a list to hold the expired policies
        List<CollateralInsurancePolicy> expiredPolicies = new ArrayList<>();

        // Check each policy for expiration
        for (CollateralInsurancePolicy policy : policies) {
            String expireDateStr = policy.getInsuranceExpireDate();
            if (expireDateStr != null && !expireDateStr.isEmpty()) {
                try {
                    LocalDate expiryDate = LocalDate.parse(expireDateStr, DATE_FORMATTER);
                    if (expiryDate.isBefore(LocalDate.now())) {
                        expiredPolicies.add(policy);
                    }
                } catch (DateTimeParseException e) {
                    // Handle parsing error, maybe log it or skip this policy
                }
            }
        }

        return expiredPolicies;
    }



    private int calculateExpiredPolicies(String branchId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);
        int expiredCount = 0;

        // Count the number of policies that are expired
        for (CollateralInsurancePolicy policy : policies) {
            String expireDateStr = policy.getInsuranceExpireDate();
            if (expireDateStr != null && !expireDateStr.isEmpty()) {
                try {
                    LocalDate expiryDate = LocalDate.parse(expireDateStr, DATE_FORMATTER);
                    if (expiryDate.isBefore(LocalDate.now())) {
                        expiredCount++;
                    }
                } catch (DateTimeParseException e) {
                    // Handle parsing error, maybe log it or skip this policy
                }
            }
        }

        return expiredCount;
    }

    private int calculateExpiredFireExtinguishers(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
        int expiredCount = 0;

        // Count the number of fire extinguishers that are expired
        for (FireExtinguisher fire : fires) {
            String inspectionDateStr = fire.getNextInspectionDate();
            if (inspectionDateStr != null && !inspectionDateStr.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                    LocalDate inspectionDate = LocalDate.parse(inspectionDateStr, formatter);
                    if (inspectionDate.isBefore(LocalDate.now())) {
                        expiredCount++;
                    }
                } catch (DateTimeParseException e) {
                    // Handle parsing error, maybe log it or skip this fire extinguisher
                }
            }
        }

        return expiredCount;
    }


    private int calculateExpiringIn30Days(String branchId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);
        int expiringCount = 0;

        // Count the number of policies expiring in the next 30 days
        for (CollateralInsurancePolicy policy : policies) {
            String expireDateStr = policy.getInsuranceExpireDate();
            if (expireDateStr != null && !expireDateStr.isEmpty()) {
                try {
                    LocalDate expiryDate = LocalDate.parse(expireDateStr, DATE_FORMATTER);
                    if (expiryDate.isAfter(LocalDate.now()) && expiryDate.isBefore(LocalDate.now().plusDays(30))) {
                        expiringCount++;
                    }
                } catch (DateTimeParseException e) {
                    // Handle parsing error, maybe log it or skip this policy
                }
            }
        }

        return expiringCount;
    }


    private int calculateEexpiringIn30DaysExtinguiser(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
        int expiringCount = 0;

        // Count the number of policies expiring in the next 30 days
        for (FireExtinguisher fire : fires) {
            String inspectionDateString = fire.getNextInspectionDate();
            // Assuming the input date string is in ISO 8601 format
            LocalDateTime inspectionDateTime = LocalDateTime.parse(inspectionDateString, DateTimeFormatter.ISO_DATE_TIME);
            LocalDate expiryDate = inspectionDateTime.toLocalDate();
            if (expiryDate.isAfter(LocalDate.now()) && expiryDate.isBefore(LocalDate.now().plusDays(30))) {
                expiringCount++;
            }
        }

        return expiringCount;
    }

    public int getActivePoliciesByBranch(String branchId) {

        List<CollateralInsurancePolicy> activePolicies = collateralInsurancePolicyRepository.findByBranchIdAndStatusName(branchId, "Active");

        return activePolicies.size();
    }



    private int calculateTotalOutstandingCases(String branchId) {
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportByBranchId(branchId);
        int totalOutstandingCases = 0;

        // Count the number of cases with status "Outstanding"
        for (IncidentOrFraud caseItem : allCases) {
            if (caseItem.getCaseStatus().getName().equals("Outstanding")) {
                totalOutstandingCases++;
            }
        }

        return totalOutstandingCases;
    }


    public BigDecimal calculateOutstandingCasesAmount(String branchId) {
        // Fetch all cases for the branch
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportByBranchId(branchId);

        // Calculate the total outstanding amount
        BigDecimal totalOutstandingAmount = BigDecimal.ZERO;
        for (IncidentOrFraud caseItem : allCases) {
            if (caseItem.getCaseStatus().getName().equals("Outstanding")) {
                BigDecimal actualOrEstimatedAmount = new BigDecimal(caseItem.getFraudAmount());
                BigDecimal recoveredAmount = new BigDecimal(caseItem.getAmountRecovered());
                BigDecimal outstandingAmount = actualOrEstimatedAmount.subtract(recoveredAmount);
                totalOutstandingAmount = totalOutstandingAmount.add(outstandingAmount);
            }
        }

        return totalOutstandingAmount;
    }


    private int calculateClosedCases(String branchId) {
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportByBranchId(branchId);
        List<IncidentOrFraud> closedAndWrittenOffCases = new ArrayList<>();

        // Get the start and end dates for the current quarter
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();

        Month startMonth;
        if (currentMonth == Month.JANUARY || currentMonth == Month.FEBRUARY || currentMonth == Month.MARCH) {
            startMonth = Month.JANUARY;
        } else if (currentMonth == Month.APRIL || currentMonth == Month.MAY || currentMonth == Month.JUNE) {
            startMonth = Month.APRIL;
        } else if (currentMonth == Month.JULY || currentMonth == Month.AUGUST || currentMonth == Month.SEPTEMBER) {
            startMonth = Month.JULY;
        } else {
            startMonth = Month.OCTOBER;
        }

        LocalDate startQuarter = LocalDate.of(currentYear, startMonth, 1);
        LocalDate endQuarter = startQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        // Filter the cases that occurred during the current quarter and have a status of "Closed" or "Written Off"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter) && caseDate.isBefore(endQuarter) &&
                    (caseItem.getCaseStatus().getName().equals("Closed") ||
                            caseItem.getCaseStatus().getName().equals("Written Off"))) {
                closedAndWrittenOffCases.add(caseItem);
            }
        }
        return closedAndWrittenOffCases.size();
    }


    public Object[] calculateNewCasesDuringQuarter(String branchId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate today = LocalDate.now();

        // Initialize counters
        int newCasesToday = 0;
        int dueIn30Days = 0;
        int dueCases = 0;
        int outstandingEscalatedCases = 0;

        // Loop through the controls to calculate the counts
        for (DailyActivityGapControl control : controls) {


            // Check if it's a new case today
            if (isNewCaseToday(control)) {
                newCasesToday++;
            }
            // Check if it's due in the next 30 days
            if (isDueIn30Days(control)) {
                dueIn30Days++;
            }
            // Check if it's a due case
            if (isDueCase(control)) {
                dueCases++;
            }
            // Check if it's an outstanding escalated case
            if (isOutstandingEscalatedCase(control)) {
                outstandingEscalatedCases++;
            }
        }


        // Return the counts as an array
        return new Object[] { newCasesToday, dueIn30Days, dueCases, outstandingEscalatedCases };
    }

    private boolean isNewCaseToday(DailyActivityGapControl control) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate controlDate = LocalDate.parse(control.getDate(), formatter); // Assuming you have a formatter for the control's date
        return controlDate.equals(today);
    }



    private boolean isDueIn30Days(DailyActivityGapControl control) {
        String actionPlanDueDate = control.getActionPlanDueDate();
        if (actionPlanDueDate != null && !actionPlanDueDate.equalsIgnoreCase("NULL")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            LocalDate dueDate = LocalDate.parse(actionPlanDueDate, formatter);
            return dueDate.isBefore(LocalDate.now().plusDays(30));
        }
        return false;
    }


    private boolean isDueCase(DailyActivityGapControl control) {

        if (control.getActionPlanDueDate() != null) {

            return control.getActionPlanDueDate().equals(LocalDate.now().toString());
        }
        return false;
    }


    private boolean isOutstandingEscalatedCase(DailyActivityGapControl control) {
        return control.getEscalatedByManager() && control.getActivityStatus() != null &&
                control.getActivityStatus().getName().equals("Open");
    }


}






