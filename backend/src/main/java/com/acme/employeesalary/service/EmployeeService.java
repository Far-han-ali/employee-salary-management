package com.acme.employeesalary.service;


import com.acme.employeesalary.entity.Employee;
import com.acme.employeesalary.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

}
