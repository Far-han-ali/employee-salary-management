package com.acme.employeesalary.controller;

import com.acme.employeesalary.dto.SalaryRequest;
import com.acme.employeesalary.dto.SalaryResponse;
import com.acme.employeesalary.entity.Salary;
import com.acme.employeesalary.service.SalaryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {
    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }
    @GetMapping
    public List<SalaryResponse> getAllSalaries(){
        return salaryService.findAllSalaries();
    }
    @GetMapping("/{salaryId}")
    public SalaryResponse getSalaryById(@PathVariable Long salaryId){
        return salaryService.findSalaryById(salaryId);
    }
    @PostMapping
    public SalaryResponse createSalary(@Valid @RequestBody SalaryRequest request){
        return salaryService.saveSalary(request);
    }

    @GetMapping("/employee/{employeeId}")
    public List<SalaryResponse> getSalariesByEmployeeId(
            @PathVariable Long employeeId) {

        return salaryService.findSalariesByEmployeeId(employeeId);
    }

    @DeleteMapping("/{salaryId}")
    public void deleteSalary(@PathVariable Long salaryId) {

        salaryService.deleteSalary(salaryId);
    }

    @PutMapping("/{salaryId}")
    public SalaryResponse updateSalary(
            @PathVariable Long salaryId,
            @Valid @RequestBody SalaryRequest request
    ) {
        return salaryService.updateSalary(salaryId, request);
    }
}
