package com.cbo.CBO_NFOS_ICMS.services.dashboardService;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher.FireExtinguisher;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.models.dashboard.DashboardDTOBranchIc;
import com.cbo.CBO_NFOS_ICMS.models.dashboard.DashboardDTODistrictIc;
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
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardDistrictIcService {
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

    public DashboardDTODistrictIc getDashboardDataDistrictIc(Long subProcessId) {

        int totalOutstandingCases = calculateTotalOutstandingCases(subProcessId);
        Object[] newCases = calculateNewCasesDuringQuarter(subProcessId);
        int newCasesToday = (int) newCases[0];
        int dueIn30Days = (int) newCases[1];
        int dueCases = (int) newCases[2];
        int outstandingEscalatedCases =(int) newCases[3];

        int closedCases = calculateClosedCases(subProcessId);
        BigDecimal outstandingCasesAmount = calculateOutstandingCasesAmount(subProcessId);
        // Calculate insurance policy-related data
        int totalActivePolicies = getActivePoliciesByBranch(subProcessId);
        int expiringIn30Days = calculateExpiringIn30Days(subProcessId);
        int expiredPolicies = calculateExpiredPolicies(subProcessId);
        BigDecimal estimatedLossAmount = calculateEstimatedLossAmount(subProcessId);
        // Calculate results for FireExtinguisher module
        int totalFireExtinguishers = calculateTotalFireExtinguishers(subProcessId);
        int eexpiringIn30Days = calculateEexpiringIn30DaysExtinguiser(subProcessId);
        int expired = calculateExpiredFireExtinguishers(subProcessId);
        BigDecimal estimatedFineAmount = calculateEstimatedFineAmount(subProcessId);
        // Calculate results for DailyActivityGapControl module

//        BigDecimal outstandingCasesDuringMonth = calculateOutstandingCasesDuringMonth(subProcessId);
        Object[] result = calculateOutstandingCasesForBranch(subProcessId);
        BigDecimal totalFinancialOutstandingCases = (BigDecimal) result[0];
        int totalNonFinancialOutstandingCases = (int) result[1];
        BigDecimal outstandingCasesDuringMonth = (BigDecimal) result[2];
        BigDecimal financialPercentageFromLastOutstanding = (BigDecimal) result[3];
        BigDecimal nonFinancialPercentageFromLastOutstanding = (BigDecimal) result[4];
        Object[] resultt = calculateRectifiedCasesForBranch(subProcessId);
        BigDecimal totalFinancialRectifiedCases = (BigDecimal) result[0];
        int totalNonFinancialRectifiedCases = (int) resultt[1];
        BigDecimal rectifiedCasesDuringMonth = (BigDecimal) resultt[2];
        BigDecimal financialPercentageFromLastRectified = (BigDecimal) resultt[3];
        BigDecimal nonFinancialPercentageFromLastRectified = (BigDecimal) resultt[4];

        Object[] resulttt = calculateIdentifiedCasesForBranch(subProcessId);
        BigDecimal totalFinancialIdentifiedCases = (BigDecimal) resulttt[0];
        int totalNonFinancialIdentifiedCases = (int) resulttt[1];
        BigDecimal identifiedCasesDuringMonth = (BigDecimal) resulttt[2];
        BigDecimal financialPercentageFromLastIdentified = (BigDecimal) resulttt[3];
        BigDecimal nonFinancialPercentageFromLastIdentified = (BigDecimal) resulttt[4];


        // Populate the DashboardDTO
        //fraud
        DashboardDTODistrictIc dashboardDTO = new DashboardDTODistrictIc();
        dashboardDTO.setOutstandingCases(totalOutstandingCases);
        dashboardDTO.setClosedCases(closedCases);
        dashboardDTO.setOutstandingCasesAmount(outstandingCasesAmount);
        // Populate CIPMs
        dashboardDTO.setTotalActivePolicies(totalActivePolicies);
        dashboardDTO.setExpiringIn30Days(expiringIn30Days);
        dashboardDTO.setExpiredPolicies(expiredPolicies);
        dashboardDTO.setEstimatedLossAmount(estimatedLossAmount);
        // for fire
        dashboardDTO.setTotalFireExtinguishers(totalFireExtinguishers);
        dashboardDTO.setExpiringIn30DaysExtinguishers(eexpiringIn30Days);
        dashboardDTO.setExpiredExtinguishers(expired);
        dashboardDTO.setEstimatedFineAmount(estimatedFineAmount);
        // for DACGM
        dashboardDTO.setTotalOutstandingCasesForDacgmDuringTheMonth(outstandingCasesDuringMonth);
        dashboardDTO.setFinancialOutstandingCases(totalFinancialOutstandingCases);
        dashboardDTO.setNonFinancialOutstandingCases(totalNonFinancialOutstandingCases);
        dashboardDTO.setFinancialChangePercentage(financialPercentageFromLastOutstanding);
        dashboardDTO.setNonFinancialChangePercentage(nonFinancialPercentageFromLastOutstanding);
        dashboardDTO.setTotalRectifiedCasesForDacgmDuringTheMonth(rectifiedCasesDuringMonth);
        dashboardDTO.setFinancialRectifiedCases(totalFinancialRectifiedCases);
        dashboardDTO.setNonFinancialRectifiedCases(totalNonFinancialRectifiedCases);
        dashboardDTO.setFinancialChangePercentager(financialPercentageFromLastRectified);
        dashboardDTO.setNonFinancialChangePercentager(nonFinancialPercentageFromLastRectified);
        dashboardDTO.setTotalIdentifiedCasesForDacgmDuringTheMonth(identifiedCasesDuringMonth);
        dashboardDTO.setFinancialIdentifiedCases(totalFinancialIdentifiedCases);
        dashboardDTO.setNonFinancialIdentifiedCases(totalNonFinancialIdentifiedCases);
        dashboardDTO.setFinancialChangePercentagei(financialPercentageFromLastIdentified);
        dashboardDTO.setNonFinancialChangePercentagei(nonFinancialPercentageFromLastIdentified);
        dashboardDTO.setNewCasesToday(newCasesToday);
        dashboardDTO.setOutstandingEscalatedCases(outstandingEscalatedCases);
        dashboardDTO.setDueCases(dueCases);
        dashboardDTO.setDueIn30Days(dueIn30Days);

//        dashboardDTO.setNewCasesToday(newCasesToday);
//        dashboardDTO.setDueIn30Days(dueIn30Days);
//        dashboardDTO.setDueCases(dueCases);
//        dashboardDTO.setOutstandingEscalatedCases(outstandingEscalatedCases);

        return dashboardDTO;
    }

    public Object[] calculateOutstandingCasesForBranch(Long subProcessId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMBySubProcessId(subProcessId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        // Filter controls based on the current month
        controls.removeIf(control -> {
            LocalDate controlDate = LocalDate.parse(control.getDate(),formatter);
            return controlDate.getMonthValue() != LocalDate.now().getMonthValue();
        });

        // Calculate financial and non-financial outstanding cases
        BigDecimal totalFinancialOutstandingCases = BigDecimal.ZERO;
        int totalNonFinancialOutstandingCases = 0;
        BigDecimal tottalOutstandingCases = BigDecimal.ZERO;
        for (DailyActivityGapControl control : controls) {
            if (control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                if (isFinancialCategory(control)) {
                    totalFinancialOutstandingCases = totalFinancialOutstandingCases.add(getAmountInvolved(control));
                } else {
                    totalNonFinancialOutstandingCases++;
                }
                tottalOutstandingCases = tottalOutstandingCases.add(BigDecimal.ONE);
            }
        }

        // Calculate the percentage change from last month for financial and non-financial cases
        BigDecimal financialChangePercentage = calculateChangePercentage(getTotalFinancialOutstandingCasesFromLastMonth(controls), totalFinancialOutstandingCases);
        BigDecimal nonFinancialChangePercentage = calculateChangePercentage(getTotalNonFinancialOutstandingCasesFromLastMonth(controls), BigDecimal.valueOf(totalNonFinancialOutstandingCases));

        // Return all four values as an array
        return new Object[] {
                totalFinancialOutstandingCases,
                totalNonFinancialOutstandingCases,
                tottalOutstandingCases,
                financialChangePercentage,
                nonFinancialChangePercentage

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
        return control.getIrregularity().getAllSubCategory().equals("Financial");
    }

    private BigDecimal getAmountInvolved(DailyActivityGapControl control) {
        return new BigDecimal(control.getAmountInvolved());
    }

    // Placeholder methods to get total financial and non-financial outstanding cases from last month
    private BigDecimal getTotalFinancialOutstandingCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        BigDecimal totalFinancialOutstandingCases = BigDecimal.ZERO;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                    totalFinancialOutstandingCases = totalFinancialOutstandingCases.add(getAmountInvolved(control));
                }
            }
        }
        return totalFinancialOutstandingCases;
    }

    private BigDecimal getTotalNonFinancialOutstandingCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int totalNonFinancialOutstandingCases = 0;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (!isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                    totalNonFinancialOutstandingCases++;
                }
            }
        }

        return BigDecimal.valueOf(totalNonFinancialOutstandingCases);
    }

///// for rectified cases

    public Object[] calculateRectifiedCasesForBranch(Long subProcessId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMBySubProcessId(subProcessId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        // Filter controls based on the current month
        controls.removeIf(controll -> {
            LocalDate controlDate = LocalDate.parse(controll.getDate(),formatter);
            return controlDate.getMonthValue() != LocalDate.now().getMonthValue();
        });

        // Calculate financial and non-financial outstanding cases
        BigDecimal totalFinancialRectifiedCases = BigDecimal.ZERO;
        int totalNonFinancialRectifiedCases = 0;
        BigDecimal totalRectifiedCases = BigDecimal.ZERO;
        for (DailyActivityGapControl controll : controls) {
            if (controll.getActivityStatus() != null && controll.getActivityStatus().getName().equals("Closed")) {
                if (isFinancialCategory(controll)) {
                    totalFinancialRectifiedCases = totalFinancialRectifiedCases.add(getAmountInvolved(controll));
                } else {
                    totalNonFinancialRectifiedCases++;
                }
                totalRectifiedCases = totalRectifiedCases.add(BigDecimal.ONE);
            }
        }

        // Calculate the percentage change from last month for financial and non-financial cases
        BigDecimal financialChangePercentageR = calculateChangePercentageR(getTotalFinancialRectifiedCasesFromLastMonth(controls), totalFinancialRectifiedCases);
        BigDecimal nonFinancialChangePercentageR = calculateChangePercentageR(getTotalNonFinancialRectifiedasesFromLastMonth(controls), BigDecimal.valueOf(totalNonFinancialRectifiedCases));

        // Return all four values as an array
        return new Object[] {
                totalFinancialRectifiedCases,
                totalNonFinancialRectifiedCases,
                totalRectifiedCases,
                financialChangePercentageR,
                nonFinancialChangePercentageR

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
    private BigDecimal getTotalFinancialRectifiedCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        BigDecimal totalFinancialOutstandingCases = BigDecimal.ZERO;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (isFinancialCategory(control) && control.getActivityStatus() != null && control.getActivityStatus().getName().equals("Open")) {
                    totalFinancialOutstandingCases = totalFinancialOutstandingCases.add(getAmountInvolved(control));
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


    public Object[] calculateIdentifiedCasesForBranch(Long subProcessId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMBySubProcessId(subProcessId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        // Filter controls based on the current month
        controls.removeIf(control -> {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            return controlDate.getMonthValue() != LocalDate.now().getMonthValue();
        });

        // Calculate financial and non-financial identified cases
        BigDecimal totalFinancialIdentifiedCases = BigDecimal.ZERO;
        int totalNonFinancialIdentifiedCases = 0;
        BigDecimal totalIdentifiedCases = BigDecimal.ZERO;
        for (DailyActivityGapControl control : controls) {
            if (control.getActivityStatus() != null) {
                if (isFinancialCategory(control)) {
                    totalFinancialIdentifiedCases = totalFinancialIdentifiedCases.add(getAmountInvolved(control));
                } else {
                    totalNonFinancialIdentifiedCases++;
                }
                totalIdentifiedCases = totalIdentifiedCases.add(BigDecimal.ONE);
            }
        }

        // Calculate the percentage change from last month for financial and non-financial cases
        BigDecimal financialChangePercentageI = calculateChangePercentageI(getTotalFinancialIdentifiedCasesFromLastMonth(controls), totalFinancialIdentifiedCases);
        BigDecimal nonFinancialChangePercentageI = calculateChangePercentageI(getTotalNonFinancialIdentifiedCasesFromLastMonth(controls), BigDecimal.valueOf(totalNonFinancialIdentifiedCases));

        // Return all four values as an array
        return new Object[] {
                totalFinancialIdentifiedCases,
                totalNonFinancialIdentifiedCases,
                totalIdentifiedCases,
                financialChangePercentageI,
                nonFinancialChangePercentageI
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
    private BigDecimal getTotalFinancialIdentifiedCasesFromLastMonth(List<DailyActivityGapControl> controls) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        BigDecimal totalFinancialIdentifiedCases = BigDecimal.ZERO;
        for (DailyActivityGapControl control : controls) {
            LocalDate controlDate = LocalDate.parse(control.getDate(), formatter);
            if (controlDate.getMonthValue() == lastMonth.getMonthValue() && controlDate.getYear() == lastMonth.getYear()) {
                if (isFinancialCategory(control) && control.getActivityStatus() != null) {
                    totalFinancialIdentifiedCases = totalFinancialIdentifiedCases.add(getAmountInvolved(control));
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






    public BigDecimal calculateEstimatedFineAmount(Long subProcessId) {
        // Fetch the list of fire extinguishers for the branch
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherBySubProcessId(subProcessId);

        // Calculate the total fine amount
        int totalExpiredFires = calculateeExpiredFireExtinguishers(subProcessId);
        BigDecimal totalFineAmount = BigDecimal.valueOf(totalExpiredFires * 10000);

        return totalFineAmount;
    }

    private int calculateeExpiredFireExtinguishers(Long subProcessId) {
        // Fetch the list of fire extinguishers for the branch
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherBySubProcessId(subProcessId);

        // Count the number of expired fire extinguishers
        int expiredCount = 0;
        for (FireExtinguisher fire : fires) {
            // Assuming the expiry date is stored in a field called 'inspectionDate'
            LocalDate expiryDate = LocalDate.parse(fire.getInspectionDate(), DATE_FORMATTER);
            if (expiryDate.isBefore(LocalDate.now())) {
                expiredCount++;
            }
        }

        return expiredCount;
    }

    private int calculateTotalFireExtinguishers(Long subProcessId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherBySubProcessId(subProcessId);
        return fires.size();
    }

    public BigDecimal calculateEstimatedLossAmount(Long subProcessId) {
        // Fetch the list of expired policies for the branch
        List<CollateralInsurancePolicy> expiredPolicies = findExpiredPoliciesBySubProcessId(subProcessId);

        // Calculate the total sum insured for all expired policies
        BigDecimal totalLossAmount = BigDecimal.ZERO;
        for (CollateralInsurancePolicy policy : expiredPolicies) {
            BigDecimal policySumInsured = new BigDecimal(policy.getSumInsured());
            totalLossAmount = totalLossAmount.add(policySumInsured);
        }

        return totalLossAmount;
    }

    private List<CollateralInsurancePolicy> findExpiredPoliciesBySubProcessId(Long subProcessId) {
        // Fetch the list of all policies for the branch
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyBySubProcessId(subProcessId);

        // Create a list to hold the expired policies
        List<CollateralInsurancePolicy> expiredPolicies = new ArrayList<>();

        // Check each policy for expiration
        for (CollateralInsurancePolicy policy : policies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(), DATE_FORMATTER);
            if (expiryDate.isBefore(LocalDate.now())) {
                expiredPolicies.add(policy);
            }
        }

        return expiredPolicies;
    }


    private int calculateExpiredPolicies(Long subProcessId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyBySubProcessId(subProcessId);
        int expiredCount = 0;

        // Count the number of policies that are expired
        for (CollateralInsurancePolicy policy : policies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(),DATE_FORMATTER);
            if (expiryDate.isBefore(LocalDate.now())) {
                expiredCount++;
            }
        }

        return expiredCount;
    }
    private int calculateExpiredFireExtinguishers(Long subProcessId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherBySubProcessId(subProcessId);
        int expiredCount = 0;

        // Count the number of policies that are expired
        for (FireExtinguisher fire : fires) {
            LocalDate expiryDate = LocalDate.parse(fire.getInspectionDate(),DATE_FORMATTER);
            if (expiryDate.isBefore(LocalDate.now())) {
                expiredCount++;
            }
        }

        return expiredCount;
    }

    private int calculateExpiringIn30Days(Long subProcessId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyBySubProcessId(subProcessId);
        int expiringCount = 0;

        // Count the number of policies expiring in the next 30 days
        for (CollateralInsurancePolicy policy : policies) {
            LocalDate expiryDate = LocalDate.parse(policy.getInsuranceExpireDate(),DATE_FORMATTER);
            if (expiryDate.isAfter(LocalDate.now()) && expiryDate.isBefore(LocalDate.now().plusDays(30))) {
                expiringCount++;
            }
        }

        return expiringCount;
    }
    private int calculateEexpiringIn30DaysExtinguiser(Long subProcessId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherBySubProcessId(subProcessId);
        int expiringCount = 0;

        // Count the number of policies expiring in the next 30 days
        for (FireExtinguisher fire : fires) {
            LocalDate expiryDate = LocalDate.parse(fire.getInspectionDate(),DATE_FORMATTER);
            if (expiryDate.isAfter(LocalDate.now()) && expiryDate.isBefore(LocalDate.now().plusDays(30))) {
                expiringCount++;
            }
        }

        return expiringCount;
    }

    public int getActivePoliciesByBranch(Long subProcessId) {

        List<CollateralInsurancePolicy> activePolicies = collateralInsurancePolicyRepository.findBySubProcessIdAndStatusName(subProcessId, "Active");

        return activePolicies.size();
    }



    private int calculateTotalOutstandingCases(Long subProcessId) {
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportBySubProcessId(subProcessId);
        int totalOutstandingCases = 0;

        // Count the number of cases with status "Outstanding"
        for (IncidentOrFraud caseItem : allCases) {
            if (caseItem.getCaseStatus().getName().equals("Outstanding")) {
                totalOutstandingCases++;
            }
        }

        return totalOutstandingCases;
    }


    public BigDecimal calculateOutstandingCasesAmount(Long subProcessId) {
        // Fetch all cases for the branch
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportBySubProcessId(subProcessId);

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


    private int calculateClosedCases(Long subProcessId) {
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportBySubProcessId(subProcessId);
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
        System.out.println("closed&writtenOff" + closedAndWrittenOffCases.size());
        return closedAndWrittenOffCases.size();
    }


    public Object[] calculateNewCasesDuringQuarter(Long subProcessId) {
        // Fetch daily activity gap controls for the branch
        List<DailyActivityGapControl> controls = dailyActivityGapControlRepository.findDACGMBySubProcessId(subProcessId);
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






