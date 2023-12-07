package com.cbo.CBO_NFOS_ICMS.controllers.UserAndEmployeeController;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Module;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Role;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles(Module module) {
        List<Role> roles = roleService.findAllRole(module);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Role> getRoleId(@PathVariable("id") Long id) {
        Role role = roleService.findRoleById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        Role newRole = roleService.addRole(role);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        Role updateRole = roleService.updateRole(role);
        return new ResponseEntity<>(updateRole, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
