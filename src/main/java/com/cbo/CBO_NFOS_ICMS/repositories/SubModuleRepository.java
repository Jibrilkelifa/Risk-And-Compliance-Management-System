package com.cbo.CBO_NFOS_ICMS.repositories;

import com.cbo.CBO_NFOS_ICMS.models.SubModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubModuleRepository extends JpaRepository<SubModule,Long> {
    Optional<SubModule> findSubModuleById(Long id);
}
