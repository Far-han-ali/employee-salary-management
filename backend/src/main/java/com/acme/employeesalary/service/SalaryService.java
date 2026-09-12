package com.acme.employeesalary.service;

import com.acme.employeesalary.entity.Salary;
import com.acme.employeesalary.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryService {
    private final SalaryRepository salaryRepository;


    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }
    public List<Salary> findAllSalaries() {
        return salaryRepository.findAll();
    }
    public Salary findSalaryById(Long salaryId){
        return salaryRepository.findById(salaryId).orElse(null);
    }
    public Salary saveSalary(Salary salary){
        return salaryRepository.save(salary);
    }
}
