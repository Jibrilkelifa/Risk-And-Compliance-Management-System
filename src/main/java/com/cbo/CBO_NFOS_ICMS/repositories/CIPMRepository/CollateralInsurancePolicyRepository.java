package com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CollateralInsurancePolicyRepository extends JpaRepository<CollateralInsurancePolicy, Long> {
    void deleteCollateralInsurancePolicyById(Long id);

    Optional<CollateralInsurancePolicy> findCollateralInsurancePolicyById(Long id);

    List<CollateralInsurancePolicy> findCollateralInsurancePolicyByBranchId(Long branchId);

//    List<CollateralInsurancePolicy> findCollateralInsurancePolicyBySubProcess(SubProcess subproces);

    @Query("SELECT COUNT(c) FROM CollateralInsurancePolicy c")
    long count();

    List<CollateralInsurancePolicy> findAllByInsuranceExpireDateBefore(LocalDate currentDate);

    List<CollateralInsurancePolicy> findByInsuranceExpireDateBetween(LocalDate currentDate, LocalDate thirtyDaysFromNow);

    List<CollateralInsurancePolicy> findCollateralInsurancePolicyBySubProcessId(Long subProcessId);

}
