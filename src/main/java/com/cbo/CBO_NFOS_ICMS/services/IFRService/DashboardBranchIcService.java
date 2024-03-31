package com.cbo.CBO_NFOS_ICMS.services.IFRService;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher.FireExtinguisher;
import com.cbo.CBO_NFOS_ICMS.models.IFR.DashboardDTOBranchIc;
import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.CollateralInsurancePolicyRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository.DailyActivityGapControlRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.FireExtinguisherRepository.FireExtinguisherRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository.IncidentOrFraudRepository;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.CollateralInsurancePolicyService;
import com.cbo.CBO_NFOS_ICMS.services.DACGMService.DailyActivityGapControlService;
import com.cbo.CBO_NFOS_ICMS.services.FireExtinguisherService.FireExtinguisherService;
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
    private static  DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    @Autowired
    private CollateralInsurancePolicyRepository collateralInsurancePolicyRepository;

    public DashboardDTOBranchIc getDashboardData(String branchId) {

        int totalOutstandingCases = calculateTotalOutstandingCases(branchId);
        int newCasesDuringQuarter = calculateNewCasesDuringQuarter(branchId);
        int closedCases = calculateClosedCases(branchId);
        BigDecimal outstandingCasesAmount = calculateOutstandingCasesAmount(branchId);
        // Calculate insurance policy-related data
        int totalActivePolicies = calculateTotalActivePolicies(branchId);
        int expiringIn30Days = calculateExpiringIn30Days(branchId);
        int expiredPolicies = calculateExpiredPolicies(branchId);
        BigDecimal estimatedLossAmount = calculateEstimatedLossAmount(branchId);
        // Calculate results for FireExtinguisher module
        int totalFireExtinguishers = calculateTotalFireExtinguishers(branchId);
        int eexpiringIn30Days = calculateEexpiringIn30DaysExtinguiser(branchId);
        int expired = calculateExpiredFireExtinguishers(branchId);
        BigDecimal estimatedFineAmount = calculateEstimatedFineAmount(branchId);
        // Calculate results for DailyActivityGapControl module
        int identifiedCasesDuringMonth = calculateIdentifiedCasesDuringMonth(branchId);
//        int rectifiedCasesDuringMonth = calculateRectifiedCasesDuringMonth(branchId);
//        int totalOutstandingCasesDacgm = calculateTotalOutstandingCasesDacgm(branchId);
//        int newCasesToday = calculateNewCasesToday(branchId);
//        int dueIn30Days = calculateDueIn30Days(branchId);
//        int dueCases = calculateDueCases(branchId);
//        int outstandingEscalatedCases = calculateOutstandingEscalatedCases(branchId);

        // Populate the DashboardDTO
        DashboardDTOBranchIc dashboardDTO = new DashboardDTOBranchIc();
        dashboardDTO.setTotalOutstandingCases(totalOutstandingCases);
        dashboardDTO.setNewCasesDuringQuarter(newCasesDuringQuarter);
        dashboardDTO.setClosedCases(closedCases);
        dashboardDTO.setOutstandingCasesAmount(outstandingCasesAmount);
        // Populate CIPMs
        dashboardDTO.setTotalActivePolicies(totalActivePolicies);
        dashboardDTO.setExpiringIn30Days(expiringIn30Days);
        dashboardDTO.setExpiredPolicies(expiredPolicies);
        dashboardDTO.setEstimatedLossAmount(estimatedLossAmount);
        // for fire
        dashboardDTO.setTotalFireExtinguishers(totalFireExtinguishers);
        dashboardDTO.setExpiringIn30DaysExtinguishers(expiringIn30Days);
        dashboardDTO.setExpiredExtinguishers(expired);
        dashboardDTO.setEstimatedFineAmount(estimatedFineAmount);
        // for DACGM
        dashboardDTO.setIdentifiedCasesDuringMonth(identifiedCasesDuringMonth);
//        dashboardDTO.setRectifiedCasesDuringMonth(rectifiedCasesDuringMonth);
        dashboardDTO.setTotalOutstandingCases(totalOutstandingCases);
//        dashboardDTO.setNewCasesToday(newCasesToday);
//        dashboardDTO.setDueIn30Days(dueIn30Days);
//        dashboardDTO.setDueCases(dueCases);
//        dashboardDTO.setOutstandingEscalatedCases(outstandingEscalatedCases);

        return dashboardDTO;
    }

//    private int calculateTotalOutstandingCasesDacgm(String branchId) {
//        LocalDate startDate = LocalDate.now().withDayOfMonth(1); // First day of the current month
//        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // Last day of the current month
//
//        List<DailyActivityGapControl> outstandingCases = dailyActivityGapControlRepository.findByBranchIdAndActivityStatusAndDateBetween(branchId, "Open", startDate, endDate);
//
//        return outstandingCases.size();
//    }

//    private int calculateRectifiedCasesDuringMonth(String branchId) {
//        // Fetch closed cases for the given branch and current month
//        LocalDate startDate = LocalDate.now().withDayOfMonth(1); // First day of the current month
//        LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // Last day of the current month
//
//        List<DailyActivityGapControl> rectifiedCases = dailyActivityGapControlRepository.findByBranchIdAndActivityStatusAndDateBetween(branchId, "Closed", startDate, endDate);
//
//        // Count the total number of closed cases
//        return rectifiedCases.size();
//    }

//    private int calculateNewCasesToday(String branchId) {
//        // Implement logic to calculate new cases registered today for DailyActivityGapControl
//        // Count the number of new cases registered today
//    }
//
//    private int calculateDueIn30Days(String branchId) {
//        // Implement logic to calculate cases due in the next 30 days for DailyActivityGapControl
//        // Count the number of cases due in 0-30 days from today's date
//    }
//
//    private int calculateDueCases(String branchId) {
//        // Implement logic to calculate cases due today for DailyActivityGapControl
//        // Count the number of cases due today
//    }
//
//    private int calculateOutstandingEscalatedCases(String branchId) {
//        // Implement logic to calculate outstanding escalated cases with active status for today
//        // Count the number of outstanding escalated cases with active status
//    }
    private int calculateIdentifiedCasesDuringMonth(String branchId) {
        List<DailyActivityGapControl> dacgms = dailyActivityGapControlRepository.findDACGMByBranchId(branchId);
        return dacgms.size();
    }

    private BigDecimal calculateEstimatedFineAmount(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
        BigDecimal totalFineAmount = BigDecimal.ZERO;

        // Calculate the sum of estimated loss amount for all policies
        for (FireExtinguisher fire : fires) {
            BigDecimal fireFineAmount = new BigDecimal(fire.getDaysLeftForInspection().intValue());
            totalFineAmount = totalFineAmount.add(fireFineAmount);
        }

        return totalFineAmount;
    }

    private int calculateTotalFireExtinguishers(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
        return fires.size();
    }

    private BigDecimal calculateEstimatedLossAmount(String branchId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);
        BigDecimal totalLossAmount = BigDecimal.ZERO;

        // Calculate the sum of estimated loss amount for all policies
        for (CollateralInsurancePolicy policy : policies) {
            BigDecimal policyLossAmount = new BigDecimal(policy.getSumInsured());
            totalLossAmount = totalLossAmount.add(policyLossAmount);
        }

        return totalLossAmount;
    }

    private int calculateExpiredPolicies(String branchId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);
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
    private int calculateExpiredFireExtinguishers(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
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

    private int calculateExpiringIn30Days(String branchId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchId(branchId);
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
    private int calculateEexpiringIn30DaysExtinguiser(String branchId) {
        List<FireExtinguisher> fires = fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
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

    private int calculateTotalActivePolicies(String branchId) {
        List<CollateralInsurancePolicy> policies = collateralInsurancePolicyRepository.findCollateralInsurancePolicyByBranchIdAndStatusName(branchId, "Active");
        return policies.size();
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


    private BigDecimal calculateOutstandingCasesAmount(String branchId) {
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportByBranchId(branchId);
        BigDecimal totalOutstandingAmount = BigDecimal.ZERO;

        // Sum up the fraud amounts for cases with status "Outstanding"
        for (IncidentOrFraud caseItem : allCases) {
            if (caseItem.getCaseStatus().getName().equals("Outstanding")) {
                BigDecimal fraudAmount = new BigDecimal(caseItem.getFraudAmount().replaceAll(",", ""));
                totalOutstandingAmount = totalOutstandingAmount.add(fraudAmount);
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
        System.out.println("closed&writtenOff" + closedAndWrittenOffCases.size());
        return closedAndWrittenOffCases.size();
    }


    private int calculateNewCasesDuringQuarter(String branchId) {
        List<IncidentOrFraud> allCases = incidentOrFraudRepository.findIncidentFraudReportByBranchId(branchId);
        List<IncidentOrFraud> newCasesDuringQuarter = new ArrayList<>();

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        for (IncidentOrFraud caseItem : allCases) {
            LocalDate caseDate = LocalDate.parse(caseItem.getFraudDetectionDate(), formatter);
            if (caseDate.isAfter(startQuarter.minusDays(1)) && caseDate.isBefore(endQuarter.plusDays(1))) {
                newCasesDuringQuarter.add(caseItem);
            }
        }
        System.out.println("newCasesDuringQuarter: " + newCasesDuringQuarter.size());
        return newCasesDuringQuarter.size();
    }
}





