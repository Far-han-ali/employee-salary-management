package com.acme.employeesalary.repository;

import com.acme.employeesalary.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
