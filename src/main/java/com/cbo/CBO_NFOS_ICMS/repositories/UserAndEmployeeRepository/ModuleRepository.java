package com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findModuleById(Long id);

    Optional<Module> findModuleByName(String name);

    Optional<Module> findByName(String name);
}
