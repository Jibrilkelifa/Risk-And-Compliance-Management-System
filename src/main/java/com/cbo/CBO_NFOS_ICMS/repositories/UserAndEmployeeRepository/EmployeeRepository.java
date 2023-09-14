package com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeById(Long id);
}
