package com.acme.employeesalary.service;

import com.acme.employeesalary.dto.EmployeeRequest;
import com.acme.employeesalary.dto.EmployeeResponse;
import com.acme.employeesalary.entity.Employee;
import com.acme.employeesalary.exception.ResourceNotFoundException;
import com.acme.employeesalary.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldCreateEmployeeSuccessfully() {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeCode("EMP100");
        request.setFirstName("Amit");
        request.setLastName("Verma");
        request.setEmail("amit.verma@example.com");
        request.setCountry("IN");
        request.setDepartment("Engineering");
        request.setJobTitle("Backend Developer");
        request.setEmploymentStatus("ACTIVE");

        Employee savedEmployee = new Employee();

        savedEmployee.setEmployeeId(100L);
        savedEmployee.setEmployeeCode("EMP100");
        savedEmployee.setFirstName("Amit");
        savedEmployee.setLastName("Verma");
        savedEmployee.setEmail("amit.verma@example.com");
        savedEmployee.setCountry("IN");
        savedEmployee.setDepartment("Engineering");
        savedEmployee.setJobTitle("Backend Developer");
        savedEmployee.setEmploymentStatus("ACTIVE");

        when(employeeRepository.existsByEmployeeCode("EMP100"))
                .thenReturn(false);

        when(employeeRepository.existsByEmail("amit.verma@example.com"))
                .thenReturn(false);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response = employeeService.saveEmployee(request);

        assertEquals(100L, response.getEmployeeId());
        assertEquals("EMP100", response.getEmployeeCode());
        assertEquals("Amit", response.getFirstName());
        assertEquals("amit.verma@example.com", response.getEmail());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void shouldRejectDuplicateEmployeeCode() {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeCode("EMP100");
        request.setFirstName("Amit");
        request.setLastName("Verma");
        request.setEmail("another.email@example.com");
        request.setCountry("IN");
        request.setDepartment("Engineering");
        request.setJobTitle("Backend Developer");
        request.setEmploymentStatus("ACTIVE");

        when(employeeRepository.existsByEmployeeCode("EMP100"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.saveEmployee(request)
        );

        assertEquals(
                "Employee code already exists: EMP100",
                exception.getMessage()
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }
    @Test
    void shouldRejectDuplicateEmail() {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeCode("EMP101");
        request.setFirstName("Rahul");
        request.setLastName("Sharma");
        request.setEmail("existing.email@example.com");
        request.setCountry("IN");
        request.setDepartment("Engineering");
        request.setJobTitle("Backend Developer");
        request.setEmploymentStatus("ACTIVE");

        when(employeeRepository.existsByEmployeeCode("EMP101"))
                .thenReturn(false);

        when(employeeRepository.existsByEmail("existing.email@example.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.saveEmployee(request)
        );

        assertEquals(
                "Email already exists: existing.email@example.com",
                exception.getMessage()
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }
    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        Long employeeId = 999L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.findEmployeeById(employeeId)
        );

        assertEquals(
                "Employee not found with id: 999",
                exception.getMessage()
        );
    }
}