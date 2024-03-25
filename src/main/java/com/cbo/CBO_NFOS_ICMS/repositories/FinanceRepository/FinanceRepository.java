package com.cbo.CBO_NFOS_ICMS.repositories.FinanceRepository;

import com.cbo.CBO_NFOS_ICMS.models.Finance.Finance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinanceRepository extends JpaRepository<Finance, Long> {
    void deleteFinanceById(Long id);

    Optional<Finance> findFinanceById(Long id);
    List<Finance> findFinanceByBranchId(Long branchId);

    List<Finance> findFinanceBySubProcessId(Long subprocessId);

    boolean existsByCaseId(String caseId);

    List<Finance> findFinanceByTeamId(Long id);
}
