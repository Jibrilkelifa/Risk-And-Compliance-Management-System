package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.ERole;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Module;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Role;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    public Role addRole(Role role) {

        return roleRepository.save(role);
    }

    public List<Role> findAllRole(Module module) {
        return roleRepository.findRoleByModule(module);
    }

    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findRoleById(Long id) {
        return roleRepository.findRoleById(id)
                .orElseThrow(() -> new UserNotFoundException("Role by id = " + id + " was not found"));
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Role findByName(ERole name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException("Role by name = " + name + " was not found"));
    }
}
