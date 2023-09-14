package com.cbo.CBO_NFOS_ICMS.repositories.DCQRepository;

import com.cbo.CBO_NFOS_ICMS.models.DCQ.ActionTaken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionTakenRepository extends JpaRepository<ActionTaken, Long> {
    Optional<ActionTaken> findActionTakenById(Long id);
}
