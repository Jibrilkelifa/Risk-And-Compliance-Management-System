package com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.ERole;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Module;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleById(Long id);

    Optional<Role> findByName(ERole name);

    List<Role> findRoleByModule(Module module);
}
