package com.cbo.CBO_NFOS_ICMS.repositories.FinanceRepository;



import com.cbo.CBO_NFOS_ICMS.models.Finance.FinanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinanceStatusRepository extends JpaRepository<FinanceStatus, Long> {

    Optional<FinanceStatus> findStatusById(Long id);
}
