package com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.SuspectedFraudsterProfession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuspectedFraudsterProfessionRepository extends JpaRepository<SuspectedFraudsterProfession, Long> {
    Optional<SuspectedFraudsterProfession>findSuspectedFraudsterProfessionById(Long id);
}
