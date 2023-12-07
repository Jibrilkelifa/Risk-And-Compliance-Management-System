package com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository;

import com.cbo.CBO_NFOS_ICMS.models.IFR.FraudType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FraudTypeRepository extends JpaRepository<FraudType, Long> {
    Optional<FraudType> findFraudTypeById(Long id);
}
