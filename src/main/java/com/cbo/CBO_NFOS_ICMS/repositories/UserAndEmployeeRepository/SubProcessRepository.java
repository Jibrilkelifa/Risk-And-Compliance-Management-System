package com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubProcessRepository extends JpaRepository<SubProcess, Long> {
    Optional<SubProcess> findSubProcessById(Long id);

    SubProcess findByName(String name);

    Optional<SubProcess> findSubProcessByName(String name);
}
