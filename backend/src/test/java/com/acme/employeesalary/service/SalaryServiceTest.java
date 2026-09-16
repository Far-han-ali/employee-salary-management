package com.acme.employeesalary.service;

import com.acme.employeesalary.dto.SalaryRequest;
import com.acme.employeesalary.dto.SalaryResponse;
import com.acme.employeesalary.entity.Employee;
import com.acme.employeesalary.entity.Salary;
import com.acme.employeesalary.exception.ResourceNotFoundException;
import com.acme.employeesalary.repository.EmployeeRepository;
import com.acme.employeesalary.repository.SalaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalaryServiceTest {

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SalaryService salaryService;

    @Test
    void shouldCreateSalarySuccessfully() {

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("75000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        request.setEffectiveTo(null);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        Salary savedSalary = new Salary();

        savedSalary.setSalaryId(10L);
        savedSalary.setEmployee(employee);
        savedSalary.setAmount(new BigDecimal("75000.00"));
        savedSalary.setCurrency("INR");
        savedSalary.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        savedSalary.setEffectiveTo(null);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(salaryRepository.findByEmployeeEmployeeId(1L))
                .thenReturn(Collections.emptyList());

        when(salaryRepository.save(any(Salary.class)))
                .thenReturn(savedSalary);

        SalaryResponse response = salaryService.saveSalary(request);

        assertEquals(10L, response.getSalaryId());
        assertEquals(1L, response.getEmployeeId());
        assertEquals(new BigDecimal("75000.00"), response.getAmount());
        assertEquals("INR", response.getCurrency());

        verify(salaryRepository).save(any(Salary.class));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(999L);
        request.setAmount(new BigDecimal("75000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        request.setEffectiveTo(null);

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> salaryService.saveSalary(request)
        );

        assertEquals(
                "Employee not found with id: 999",
                exception.getMessage()
        );

        verify(salaryRepository, never())
                .save(any(Salary.class));
    }

    @Test
    void shouldRejectOverlappingSalaryPeriod() {

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("80000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 10, 1));
        request.setEffectiveTo(LocalDate.of(2026, 12, 31));

        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        Salary existingSalary = new Salary();

        existingSalary.setSalaryId(5L);
        existingSalary.setEmployee(employee);
        existingSalary.setAmount(new BigDecimal("75000.00"));
        existingSalary.setCurrency("INR");
        existingSalary.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        existingSalary.setEffectiveTo(LocalDate.of(2026, 11, 30));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(salaryRepository.findByEmployeeEmployeeId(1L))
                .thenReturn(List.of(existingSalary));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> salaryService.saveSalary(request)
        );

        assertEquals(
                "Salary period overlaps with an existing salary record",
                exception.getMessage()
        );

        verify(salaryRepository, never())
                .save(any(Salary.class));
    }

    @Test
    void shouldRejectInvalidSalaryDateRange() {

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("75000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 12, 31));
        request.setEffectiveTo(LocalDate.of(2026, 9, 1));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> salaryService.saveSalary(request)
        );

        assertEquals(
                "effectiveTo must be greater than or equal to effectiveFrom",
                exception.getMessage()
        );

        verify(employeeRepository, never())
                .findById(any(Long.class));

        verify(salaryRepository, never())
                .save(any(Salary.class));
    }

    @Test
    void shouldUpdateSalarySuccessfully() {

        Long salaryId = 10L;

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("85000.00"));
        request.setCurrency("USD");
        request.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        request.setEffectiveTo(null);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        Salary existingSalary = new Salary();

        existingSalary.setSalaryId(salaryId);
        existingSalary.setEmployee(employee);
        existingSalary.setAmount(new BigDecimal("75000.00"));
        existingSalary.setCurrency("INR");
        existingSalary.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        existingSalary.setEffectiveTo(null);

        when(salaryRepository.findById(salaryId))
                .thenReturn(Optional.of(existingSalary));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(salaryRepository.findByEmployeeEmployeeIdAndSalaryIdNot(1L, salaryId))
                .thenReturn(Collections.emptyList());

        when(salaryRepository.save(any(Salary.class)))
                .thenReturn(existingSalary);

        SalaryResponse response =
                salaryService.updateSalary(salaryId, request);

        assertEquals(salaryId, response.getSalaryId());
        assertEquals(1L, response.getEmployeeId());

        verify(salaryRepository).save(existingSalary);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingSalary() {

        Long salaryId = 999L;

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("85000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        request.setEffectiveTo(null);

        when(salaryRepository.findById(salaryId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> salaryService.updateSalary(salaryId, request)
        );

        assertEquals(
                "Salary not found with id: 999",
                exception.getMessage()
        );

        verify(salaryRepository, never())
                .save(any(Salary.class));
    }

    @Test
    void shouldDeleteSalarySuccessfully() {

        Long salaryId = 10L;

        Salary salary = new Salary();
        salary.setSalaryId(salaryId);

        when(salaryRepository.findById(salaryId))
                .thenReturn(Optional.of(salary));

        salaryService.deleteSalary(salaryId);

        verify(salaryRepository).delete(salary);
    }

    @Test
    void shouldFindSalariesByEmployeeIdSuccessfully() {

        Long employeeId = 1L;

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        Salary salary = new Salary();

        salary.setSalaryId(10L);
        salary.setEmployee(employee);
        salary.setAmount(new BigDecimal("75000.00"));
        salary.setCurrency("INR");
        salary.setEffectiveFrom(LocalDate.of(2026, 9, 1));
        salary.setEffectiveTo(null);

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        when(salaryRepository.findByEmployeeEmployeeId(employeeId))
                .thenReturn(List.of(salary));

        List<SalaryResponse> responses =
                salaryService.findSalariesByEmployeeId(employeeId);

        assertEquals(1, responses.size());
        assertEquals(10L, responses.get(0).getSalaryId());
        assertEquals(75000, responses.get(0).getAmount().intValue());
        assertEquals("INR", responses.get(0).getCurrency());
    }
}