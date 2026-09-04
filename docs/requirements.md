# Employee Salary Management System — Requirements

## 1. Goal

Build a web-based salary management system that enables ACME’s HR managers to manage and understand salary information for approximately 10,000 employees across multiple countries.

The system should replace the current Excel-based workflow with a reliable, searchable, and easy-to-use application that helps HR quickly answer questions such as “How much do we pay?”, “How is salary distributed across countries/departments?”, and “Which employees fall within a particular salary range?”

The primary goal is not to build a complex HR platform, but to provide a focused and maintainable salary management solution with a strong foundation for future growth.

## 2. User & Primary Use Case

**Primary user:** HR Manager

The HR Manager should be able to:

* View and search employees.
* View an employee’s salary details.
* Add new employees and salary information.
* Update salary information.
* Filter employees using relevant attributes such as country, department, and salary range.
* Understand salary distribution through meaningful summary information.
* Deactivate employee records where appropriate.

## 3. Scope & Features

### Employee & Salary Management

Each employee will have relevant information such as:

* Employee ID
* Name
* Email
* Country
* Department
* Job title
* Salary
* Currency
* Joining date
* Employment status

HR managers can create, view, update, and deactivate employee records.

### Search & Filtering

HR should be able to quickly find employees by:

* Name or employee ID
* Country
* Department
* Job title
* Salary range

Filtering and searching should be performed efficiently on the backend rather than loading all 10,000 records into the browser.

### Salary Insights

The system should provide a simple overview of salary data, including:

* Total employees
* Minimum and maximum salary
* Average salary by currency
* Salary distribution by country
* Salary distribution by department

Because the organization operates across multiple countries, salary values in different currencies should not be directly combined without conversion. For the initial version, salary statistics will therefore be grouped by currency.

### Data Seeding

The application will include a deterministic seed mechanism capable of generating approximately 10,000 realistic employee records across multiple countries, departments, roles, currencies, and salary ranges.

## 4. Non-Functional Requirements

* Clean separation between UI, business logic, persistence, and API layers.
* RESTful backend APIs.
* Input validation and meaningful error responses.
* Automated tests covering core business functionality.
* Efficient pagination and filtering.
* Database constraints to maintain data integrity.
* Configuration separated from application code.
* Clear logging and error handling.
* The application should be deployable and usable without local development setup.
* The UI should remain responsive when working with the full 10,000-employee dataset.

## 5. Deliberately Out of Scope

To keep the solution focused on the core problem, the following are intentionally excluded:

* Payroll processing and salary payment.
* Tax calculation.
* Benefits management.
* Attendance and leave management.
* Performance management.
* Employee self-service portals.
* Complex role-based access control beyond what is necessary for the assessment.
* Integration with external payroll or HR systems.
* Real-time notifications.
* Multi-level approval workflows.
* Advanced compensation planning and forecasting.

These features could be introduced later once the core salary-management workflow is stable.

## 6. Key Design Decisions & Assumptions

The system is designed primarily for an HR Manager rather than employees, so the initial version prioritizes efficient data management, searchability, correctness, and salary insights over a large number of HR features.

Because ACME operates across multiple countries, currency is stored explicitly with salary information. Salary analytics will not assume that numerical salary values in different currencies are directly comparable.

Pagination, server-side filtering, and indexed database queries will be used because the system is expected to handle 10,000 employees today and should have a reasonable path to support a larger dataset later.

The architecture will remain intentionally simple. A well-structured modular application is preferred over introducing microservices or other infrastructure that does not provide meaningful value for the current problem.

## 7. Definition of Done

The solution will be considered complete when:

* HR can manage employee salary records through the web UI.
* Employees can be searched and filtered efficiently.
* Salary insights are available through the UI.
* 10,000 employees can be seeded.
* Core functionality has meaningful automated test coverage.
* The application is deployed and accessible.
* A short video demonstrates the major user workflows.
* Architecture, trade-offs, AI usage, and relevant design decisions are documented.
