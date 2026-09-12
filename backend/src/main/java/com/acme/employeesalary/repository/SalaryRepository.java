package com.acme.employeesalary.repository;

import com.acme.employeesalary.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
