package com.cbo.CBO_NFOS_ICMS.repositories.FireExtinguisherRepository;

import com.cbo.CBO_NFOS_ICMS.models.Finance.Finance;
import com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher.FireExtinguisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FireExtinguisherRepository extends JpaRepository<FireExtinguisher, Long> {
    void deleteFireExtinguisherById(Long id);

    Optional<FireExtinguisher> findFireExtinguisherById(Long id);
    List<FireExtinguisher> findFireExtinguisherByBranchId(Long branchId);

    List<FireExtinguisher> findFireExtinguisherBySubProcessId(Long subprocessId);

}
