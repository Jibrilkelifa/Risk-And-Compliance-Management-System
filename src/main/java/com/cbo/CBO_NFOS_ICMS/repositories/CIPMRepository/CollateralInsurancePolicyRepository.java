package com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CollateralInsurancePolicyRepository extends JpaRepository<CollateralInsurancePolicy, Long> {
    void deleteCollateralInsurancePolicyById(Long id);

    Optional<CollateralInsurancePolicy> findCollateralInsurancePolicyById(Long id);
    List<CollateralInsurancePolicy> findCollateralInsurancePolicyByOrganizationalUnit(OrganizationalUnit organizationalUnit);
 @Query("SELECT cipm FROM CollateralInsurancePolicy cipm JOIN OrganizationalUnit b ON cipm.organizationalUnit.id = b.id JOIN SubProcess d ON b.subProcess.id = d.id WHERE d.id = :id")
   List<CollateralInsurancePolicy> findCollateralInsurancePolicyBySubProcessId(Long id);
    @Query("SELECT COUNT(c) FROM CollateralInsurancePolicy c")
    long count();
    List<CollateralInsurancePolicy> findAllByInsuranceExpireDateBefore(LocalDate currentDate);
    List<CollateralInsurancePolicy> findByInsuranceExpireDateBetween(LocalDate currentDate, LocalDate thirtyDaysFromNow);
    List<CollateralInsurancePolicy> findCollateralInsurancePolicyByOrganizationalUnit_SubProcess(SubProcess subProcess);

}
