package com.acme.employeesalary.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @NotBlank
    @Size(max = 30)
    @Column(name = "employee_code", nullable = false, unique = true, length = 30)
    private String employeeCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "country", nullable = false, length = 2)
    private String country;

    @NotBlank
    @Size(max = 100)
    @Column(name = "department",nullable = false,length = 100)
    private String department;

    @NotBlank
    @Size(max = 150)
    @Column(name = "job_title", nullable = false, length = 150)
    private String jobTitle;

    @NotBlank
    @Size(max = 30)
    @Column(name = "employment_status", nullable = false,length = 30)
    private String employmentStatus;

}
