package com.acme.employeesalary.service;

import com.acme.employeesalary.dto.EmployeeRequest;
import com.acme.employeesalary.dto.EmployeeResponse;
import com.acme.employeesalary.entity.Employee;
import com.acme.employeesalary.exception.ResourceNotFoundException;
import com.acme.employeesalary.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<EmployeeResponse> findAllEmployees(
            int page,
            int size,
            String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Employee> employees;

        if (keyword == null || keyword.isBlank()) {
            employees = employeeRepository.findAll(pageable);
        } else {
            employees = employeeRepository.searchEmployees(
                    keyword.trim(),
                    pageable
            );
        }

        return employees.map(this::convertToResponse);
    }

    public EmployeeResponse findEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId
                ));

        return convertToResponse(employee);
    }

    public EmployeeResponse saveEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException(
                    "Employee code already exists: " + request.getEmployeeCode()
            );
        }

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists: " + request.getEmail()
            );
        }

        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setCountry(request.getCountry());
        employee.setDepartment(request.getDepartment());
        employee.setJobTitle(request.getJobTitle());
        employee.setEmploymentStatus(request.getEmploymentStatus());

        LocalDateTime now = LocalDateTime.now();

        employee.setCreatedAt(now);
        employee.setUpdatedAt(now);

        Employee savedEmployee = employeeRepository.save(employee);

        return convertToResponse(savedEmployee);
    }

    public EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeRequest request
    ) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId
                ));

        if (employeeRepository.existsByEmployeeCodeAndEmployeeIdNot(
                request.getEmployeeCode(),
                employeeId
        )) {
            throw new IllegalArgumentException(
                    "Employee code already exists: " + request.getEmployeeCode()
            );
        }

        if (employeeRepository.existsByEmailAndEmployeeIdNot(
                request.getEmail(),
                employeeId
        )) {
            throw new IllegalArgumentException(
                    "Email already exists: " + request.getEmail()
            );
        }

        existingEmployee.setEmployeeCode(request.getEmployeeCode());
        existingEmployee.setFirstName(request.getFirstName());
        existingEmployee.setLastName(request.getLastName());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setCountry(request.getCountry());
        existingEmployee.setDepartment(request.getDepartment());
        existingEmployee.setJobTitle(request.getJobTitle());
        existingEmployee.setEmploymentStatus(request.getEmploymentStatus());
        existingEmployee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return convertToResponse(updatedEmployee);
    }

    public EmployeeResponse deactivateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId
                ));

        employee.setEmploymentStatus("INACTIVE");
        employee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(employee);

        return convertToResponse(updatedEmployee);
    }

    private EmployeeResponse convertToResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId(employee.getEmployeeId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setCountry(employee.getCountry());
        response.setDepartment(employee.getDepartment());
        response.setJobTitle(employee.getJobTitle());
        response.setEmploymentStatus(employee.getEmploymentStatus());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());

        return response;
    }
}