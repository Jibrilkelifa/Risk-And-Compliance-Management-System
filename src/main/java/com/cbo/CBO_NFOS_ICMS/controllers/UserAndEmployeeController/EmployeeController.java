package com.cbo.CBO_NFOS_ICMS.controllers.UserAndEmployeeController;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Employee;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        List<Employee> employees = employeeService.findAllEmployee();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'SUPER_ADMIN')")
    public Employee getEmployeeById(@PathVariable("id") Long id){
        return employeeService.findEmployeeById(id);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deleteEmployee(@PathVariable("id") Long id){

        employeeService.deleteEmployee(id);
    }
}
