package com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository;

import com.cbo.CBO_NFOS_ICMS.models.IFR.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaseStatusRepository extends JpaRepository<CaseStatus, Long> {
    Optional<CaseStatus> findCaseStatusById(Long id);


}
