package com.cbo.sso.repositories;


import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.ERole;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Module;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleById(Long id);

    @Query("SELECT u FROM Role u WHERE u.name = ?1")
    public Role findByName(ERole name);
    List<Role> findRoleByModule(Module module);

    // Add this method to fetch all roles
    @Query("SELECT r FROM Role r")
    List<Role> findAllRoles();

}
