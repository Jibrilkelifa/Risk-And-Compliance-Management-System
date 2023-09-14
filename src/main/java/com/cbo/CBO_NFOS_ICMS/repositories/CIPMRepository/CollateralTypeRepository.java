package com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollateralTypeRepository extends JpaRepository<CollateralType, Long> {
    Optional<CollateralType> findCollateralTypeById(Long id);
}
