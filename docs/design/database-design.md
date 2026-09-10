# Database Design



## 1. Overview



The system manages employee and salary information for ACME, an organization with approximately 10,000 employees across multiple countries.



The database is designed to support salary management, salary history, filtering, and organizational salary insights while keeping the model simple and maintainable.



## 2. Entity Relationship



Employee 1 ---- * Salary



An employee can have multiple salary records because salary changes over time.



## 3. Employee



| Column | Type | Constraints | Description |

|---|---|---|---|

| employee_id | BIGINT | PK | Unique employee identifier |

| employee_code | VARCHAR(30) | UNIQUE, NOT NULL | Business identifier for the employee |

| first_name | VARCHAR(100) | NOT NULL | Employee first name |

| last_name | VARCHAR(100) | NOT NULL | Employee last name |

| email | VARCHAR(255) | UNIQUE, NOT NULL | Employee email |

| country | CHAR(2) | NOT NULL | ISO-style country code |

| department | VARCHAR(100) | NOT NULL | Employee department |

| job_title | VARCHAR(150) | NOT NULL | Employee job title |

| employment_status | VARCHAR(30) | NOT NULL | Employment status |

| created_at | TIMESTAMP | NOT NULL | Record creation time |

| updated_at | TIMESTAMP | NOT NULL | Last update time |



## 4. Salary



| Column | Type | Constraints | Description |

|---|---|---|---|

| salary_id | BIGINT | PK | Unique salary record |

| employee_id | BIGINT | FK, NOT NULL | References Employee |

| amount | NUMERIC(19,4) | NOT NULL | Salary amount |

| currency | CHAR(3) | NOT NULL | ISO-style currency code |

| effective_from | DATE | NOT NULL | Date from which salary applies |

| effective_to | DATE | NULL | Date until which salary applies |

| created_at | TIMESTAMP | NOT NULL | Record creation time |



## 5. Salary History



Salary is modeled as a separate entity rather than a field on Employee.



This preserves historical salary information. When an employee receives a salary change, a new salary record is created instead of overwriting the previous record.



Example:



Employee EMP001:



* 2023: USD 60000
* 2024: USD 68000
* 2025: USD 75000



This allows HR to view both current and historical salary information.



## 6. Money and Currency



Salary amounts use PostgreSQL NUMERIC rather than floating-point types to avoid monetary precision issues.



Currency is stored as a three-character currency code such as INR, USD, EUR, or GBP.



Currency conversion is deliberately outside the initial scope. Salary insights compare and aggregate salaries within the same currency unless an explicit conversion capability is added later.



## 7. Country



Country is represented using a two-character country code, such as IN, US, GB, or DE.



This supports employees across multiple countries while keeping filtering and grouping consistent.



## 8. Indexing



Indexes will be considered for fields commonly used by HR queries, including:



* employee_code
* email
* country
* department
* employment_status
* salary.employee_id
* salary.effective_from



The goal is to support efficient searching and salary-history queries for approximately 10,000 employees.



## 9. Deliberately Out of Scope



The initial system will not implement:



* Payroll processing
* Tax calculation
* Benefits management
* Bonus calculation
* Payslip generation
* Employee self-service
* Currency conversion
* Complex authentication and authorization



These features are outside the core salary-management problem and would increase complexity without being necessary to demonstrate the required product capabilities.



## 10. Design Rationale



The design favors a small relational model with clear ownership and historical salary records.



The main priorities are:



* Maintainability
* Data integrity
* Salary history
* Support for multiple countries and currencies
* Efficient HR queries
* Simplicity appropriate for the assessment scope


