package com.acme.employeesalary.repository;

import com.acme.employeesalary.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findByEmployeeEmployeeId(Long employeeId);
    List<Salary> findByEmployeeEmployeeIdAndSalaryIdNot(
            Long employeeId,
            Long salaryId
    );
}
