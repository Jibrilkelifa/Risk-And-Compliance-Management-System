package com.cbo.CBO_NFOS_ICMS.repositories.IFBRepository;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralInsurancePolicy;
import com.cbo.CBO_NFOS_ICMS.models.IFB.IFB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFBRepository extends JpaRepository<IFB, Long> {
    void deleteIFBById(Long id);

    Optional<IFB> findIFBById(Long id);
    List<IFB> findIFBByBranchId(Long branchId);

    List<IFB> findIFBBySubProcessId(Long subprocessId);

    boolean existsByCaseId(String caseId);

}
