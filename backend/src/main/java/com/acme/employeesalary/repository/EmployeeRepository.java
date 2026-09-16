package com.acme.employeesalary.repository;

import com.acme.employeesalary.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCodeAndEmployeeIdNot(
            String employeeCode,
            Long employeeId
    );

    boolean existsByEmailAndEmployeeIdNot(
            String email,
            Long employeeId
    );
    @Query("""
            SELECT e FROM Employee e
            WHERE LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Employee> searchEmployees(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}

