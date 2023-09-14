package com.cbo.CBO_NFOS_ICMS.repositories.DCQRepository;

import com.cbo.CBO_NFOS_ICMS.models.DCQ.ChequeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChequeTypeRepository extends JpaRepository<ChequeType, Long> {
    Optional<ChequeType> findChequeTypeById(Long id);
}
