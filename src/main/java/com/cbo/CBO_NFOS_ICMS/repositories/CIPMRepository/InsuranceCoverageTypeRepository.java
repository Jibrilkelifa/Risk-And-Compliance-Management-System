package com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.InsuranceCoverageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceCoverageTypeRepository extends JpaRepository<InsuranceCoverageType, Long> {
    Optional<InsuranceCoverageType> findInsuranceCoverageTypeById(Long id);

}
