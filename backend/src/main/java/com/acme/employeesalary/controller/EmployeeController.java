package com.acme.employeesalary.controller;

import com.acme.employeesalary.dto.EmployeeRequest;
import com.acme.employeesalary.dto.EmployeeResponse;
import com.acme.employeesalary.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public Page<EmployeeResponse> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        return employeeService.findAllEmployees(page, size, keyword);
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponse getEmployeeById(
            @PathVariable Long employeeId
    ) {
        return employeeService.findEmployeeById(employeeId);
    }

    @PostMapping
    public EmployeeResponse createEmployee(
            @Valid @RequestBody EmployeeRequest request
    ) {
        return employeeService.saveEmployee(request);
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeRequest request
    ) {
        return employeeService.updateEmployee(employeeId, request);
    }

    @DeleteMapping("/{employeeId}")
    public EmployeeResponse deactivateEmployee(
            @PathVariable Long employeeId
    ) {
        return employeeService.deactivateEmployee(employeeId);
    }
}