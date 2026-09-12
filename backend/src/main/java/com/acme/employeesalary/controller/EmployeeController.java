package com.acme.employeesalary.controller;

import com.acme.employeesalary.entity.Employee;
import com.acme.employeesalary.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAllEmployees();
    }
    @GetMapping("/{employeeId}")
    public Employee getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.findEmployeeById(employeeId);
    }
    @PostMapping
    public Employee createEmployee(@Valid @RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }


}
