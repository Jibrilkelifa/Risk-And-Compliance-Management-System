//package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;
//
//import com.cbo.CBO_NFOS_ICMS.exception.NoSuchUserExistsException;
//import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Employee;
//import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.EmployeeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class EmployeeService {
//    private final EmployeeRepository employeeRepository;
//
//    @Autowired
//    public EmployeeService(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//    public List<Employee> findAllEmployee() {
//        return employeeRepository.findAll();
//    }
//
//    public Employee findEmployeeById(Long id) {
//        return employeeRepository.findById(id).orElseThrow(() -> new NoSuchUserExistsException("NO Employee PRESENT WITH ID = " + id));
//    }
//
//    public String deleteEmployee(Long id) {
//
//        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
//        if (existingEmployee == null)
//            throw new NoSuchUserExistsException("No Such Employee exists!!");
//        else {
//            employeeRepository.deleteById(id);
//            return "Record deleted Successfully";
//        }
//    }
//}
