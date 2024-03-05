package com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository;

import com.cbo.CBO_NFOS_ICMS.models.IFR.IncidentOrFraud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IncidentOrFraudRepository extends JpaRepository<IncidentOrFraud, Long> {
    Optional<IncidentOrFraud> findIncidentFraudReportById(Long id);
    @Query(value = "SELECT MAX(case_number) FROM your_table_name", nativeQuery = true)
    Integer getLatestCaseNumber();

    List<IncidentOrFraud> findIncidentFraudReportByBranchId(Long id);
    boolean existsByCaseId(String caseId);

    List<IncidentOrFraud> findAllByFraudDetectionDateBetween(String startDate, String endDate);

    int countAllByFraudDetectionDateBetween(String startDate, String endDate);

    @Query("SELECT i.fraudType.name, COUNT(i) FROM IncidentOrFraud i WHERE i.fraudDetectionDate BETWEEN :startDate AND :endDate GROUP BY i.fraudType.name")
    List<Object[]> findNumberOfIncidentOrFraudCasesLastWeekByFraudType(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<IncidentOrFraud> findIncidentFraudReportBySubProcessId(Long subProcessId);
}
