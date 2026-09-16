package com.acme.employeesalary.service;

import com.acme.employeesalary.dto.SalaryRequest;
import com.acme.employeesalary.dto.SalaryResponse;
import com.acme.employeesalary.entity.Employee;
import com.acme.employeesalary.entity.Salary;
import com.acme.employeesalary.exception.ResourceNotFoundException;
import com.acme.employeesalary.repository.EmployeeRepository;
import com.acme.employeesalary.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalaryService {
    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;


    public SalaryService(SalaryRepository salaryRepository, EmployeeRepository employeeRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<SalaryResponse> findAllSalaries() {
        return salaryRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public SalaryResponse findSalaryById(Long salaryId) {
        Salary salary = salaryRepository.findById(salaryId).orElseThrow(() -> new ResourceNotFoundException("Salary not found with id : " + salaryId));
        return convertToResponse(salary);
    }

    public SalaryResponse saveSalary(SalaryRequest request) {

        if (request.getEffectiveTo() != null
                && request.getEffectiveTo().isBefore(request.getEffectiveFrom())) {

            throw new IllegalArgumentException(
                    "effectiveTo must be greater than or equal to effectiveFrom"
            );
        }

        if (request.getEffectiveTo() != null
                && request.getEffectiveTo().isEqual(request.getEffectiveFrom())) {

            throw new IllegalArgumentException(
                    "effectiveTo must be after effectiveFrom"
            );
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + request.getEmployeeId()
                ));

        validateNoOverlap(request);

        Salary salary = new Salary();

        salary.setEmployee(employee);
        salary.setAmount(request.getAmount());
        salary.setCurrency(request.getCurrency());
        salary.setEffectiveFrom(request.getEffectiveFrom());
        salary.setEffectiveTo(request.getEffectiveTo());
        salary.setCreatedAt(LocalDateTime.now());

        Salary savedSalary = salaryRepository.save(salary);

        return convertToResponse(savedSalary);
    }

    private void validateNoOverlap(SalaryRequest request) {

        List<Salary> existingSalaries =
                salaryRepository.findByEmployeeEmployeeId(request.getEmployeeId());

        LocalDate newStart = request.getEffectiveFrom();
        LocalDate newEnd = request.getEffectiveTo();

        for (Salary existingSalary : existingSalaries) {

            LocalDate existingStart = existingSalary.getEffectiveFrom();
            LocalDate existingEnd = existingSalary.getEffectiveTo();

            boolean startsBeforeExistingEnds =
                    existingEnd == null || !newStart.isAfter(existingEnd);

            boolean existingStartsBeforeNewEnds =
                    newEnd == null || !existingStart.isAfter(newEnd);

            if (startsBeforeExistingEnds && existingStartsBeforeNewEnds) {
                throw new IllegalArgumentException(
                        "Salary period overlaps with an existing salary record"
                );
            }
        }
    }

    private void validateNoOverlapForUpdate(
            SalaryRequest request,
            Long salaryId
    ) {
        List<Salary> existingSalaries =
                salaryRepository.findByEmployeeEmployeeIdAndSalaryIdNot(
                        request.getEmployeeId(),
                        salaryId
                );

        LocalDate newStart = request.getEffectiveFrom();
        LocalDate newEnd = request.getEffectiveTo();

        for (Salary existingSalary : existingSalaries) {
            LocalDate existingStart = existingSalary.getEffectiveFrom();
            LocalDate existingEnd = existingSalary.getEffectiveTo();

            boolean startsBeforeExistingEnds =
                    existingEnd == null || !newStart.isAfter(existingEnd);

            boolean existingStartsBeforeNewEnds =
                    newEnd == null || !existingStart.isAfter(newEnd);

            if (startsBeforeExistingEnds && existingStartsBeforeNewEnds) {
                throw new IllegalArgumentException(
                        "Salary period overlaps with an existing salary record"
                );
            }
        }
    }

    private SalaryResponse convertToResponse(Salary salary) {
        SalaryResponse response = new SalaryResponse();
        response.setSalaryId(salary.getSalaryId());
        response.setEmployeeId(salary.getEmployee().getEmployeeId());
        response.setAmount(salary.getAmount());
        response.setCurrency(salary.getCurrency());
        response.setEffectiveFrom(salary.getEffectiveFrom());
        response.setEffectiveTo(salary.getEffectiveTo());
        response.setCreatedAt(salary.getCreatedAt());
        return response;
    }

    public List<SalaryResponse> findSalariesByEmployeeId(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId
                ));

        return salaryRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public SalaryResponse updateSalary(Long salaryId, SalaryRequest request) {

        Salary existingSalary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salary not found with id: " + salaryId
                ));


        if (request.getEffectiveTo() != null
                && !request.getEffectiveTo().isAfter(request.getEffectiveFrom())) {
            throw new IllegalArgumentException(
                    "effectiveTo must be after effectiveFrom"
            );
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + request.getEmployeeId()
                ));
        validateNoOverlapForUpdate(request, salaryId);

        existingSalary.setEmployee(employee);
        existingSalary.setAmount(request.getAmount());
        existingSalary.setCurrency(request.getCurrency());
        existingSalary.setEffectiveFrom(request.getEffectiveFrom());
        existingSalary.setEffectiveTo(request.getEffectiveTo());

        Salary updatedSalary = salaryRepository.save(existingSalary);

        return convertToResponse(updatedSalary);
    }

    public void deleteSalary(Long salaryId) {

        Salary salary = salaryRepository.findById(salaryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Salary not found with id: " + salaryId
                ));

        salaryRepository.delete(salary);
    }


}
