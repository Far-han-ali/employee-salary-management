package com.acme.employeesalary.controller;

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
    public List<Salary> getAllSalaries(){
        return salaryService.findAllSalaries();
    }
    @GetMapping("/{salaryId}")
    public Salary getSalaryById(@PathVariable Long salaryId){
        return salaryService.findSalaryById(salaryId);
    }
    @PostMapping
    public Salary createSalary(@Valid @RequestBody Salary salary){
        return salaryService.saveSalary(salary);
    }
}
