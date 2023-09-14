package com.cbo.CBO_NFOS_ICMS.repositories;

import com.cbo.CBO_NFOS_ICMS.models.AllIrregularity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllIrregularityRepository extends JpaRepository<AllIrregularity, Long> {
    Optional<AllIrregularity> findIrregularityById(Long id);
}
