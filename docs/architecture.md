# System Architecture

## 1. Overview

The Employee Salary Management System will be implemented as a modular monolith consisting of an Angular frontend, Spring Boot backend, and PostgreSQL relational database.

The architecture is intentionally simple because the current problem has a focused domain and a single primary persona: the HR Manager. The design should remain easy to understand, test, deploy, and extend without introducing unnecessary distributed-system complexity.

## 2. High-Level Architecture

```text
                    HR Manager
                         |
                         v
                 +---------------+
                 | Angular UI    |
                 |               |
                 | Dashboard     |
                 | Employee List |
                 | Employee Form |
                 +-------+-------+
                         |
                     HTTP/REST
                         |
                         v
                 +---------------+
                 | Spring Boot   |
                 |               |
                 | Controllers   |
                 | Services      |
                 | Repositories  |
                 | Validation    |
                 +-------+-------+
                         |
                         v
                 +---------------+
                 | PostgreSQL    |
                 |               |
                 | employees     |
                 +---------------+
```

## 3. Backend Architecture

The backend follows a layered architecture:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Database
```

### Controller Layer

Responsible for HTTP requests, request/response DTOs, and API-level concerns.

Controllers should remain thin and delegate business operations to services.

### Service Layer

Contains business rules and application logic, including employee creation, updates, validation of business constraints, and salary-related operations.

### Repository Layer

Responsible for persistence and database access through Spring Data JPA.

### DTO Layer

API request and response objects will be separated from persistence entities to avoid exposing database models directly through the API.

## 4. Frontend Architecture

Angular will be organized around features and shared functionality.

```text
app
├── core
│   ├── services
│   ├── interceptors
│   └── models
│
├── features
│   ├── dashboard
│   └── employees
│       ├── employee-list
│       ├── employee-form
│       └── employee-details
│
└── shared
    └── reusable components
```

The employee feature will own employee-related UI and behavior, while core contains application-wide services and shared contains reusable UI elements.

## 5. Database

PostgreSQL will be used as the relational database.

The primary employee record will contain employee identity, organizational information, salary, currency, employment status, and audit timestamps.

Salary will be represented using a decimal-compatible database type and Java `BigDecimal` rather than floating-point types to avoid monetary precision issues.

## 6. Search, Filtering and Pagination

Employee search and filtering will be performed on the backend.

Supported filters include employee name/ID, country, department, job title, and salary range.

Pagination will be applied at the API/database level rather than loading the complete employee dataset into the browser.

This approach minimizes data transfer and keeps the UI responsive.

## 7. Salary Analytics

Salary analytics will be grouped by currency when calculating monetary statistics.

The system will not directly average salaries represented in different currencies.

Currency conversion through an external exchange-rate service is intentionally excluded from the initial implementation because it introduces external dependencies, rate freshness concerns, and additional failure scenarios that are not necessary for the core assessment.

## 8. Error Handling

The backend will provide centralized exception handling and consistent API error responses.

Expected cases include:

* Employee not found
* Duplicate employee/email
* Invalid request data
* Invalid salary values
* Persistence failures

Frontend screens will provide appropriate loading, empty, validation, and error states.

## 9. Testing Strategy

The system will contain:

* Unit tests for core business logic.
* Repository/integration tests for important persistence behavior.
* API tests for important endpoint behavior.
* Angular tests for important component and user interactions.

Tests should be deterministic, fast, readable, and focused on meaningful behavior.

## 10. Scalability Considerations

The initial system is designed for approximately 10,000 employees but avoids assumptions that would prevent future growth.

The design uses:

* Server-side pagination
* Server-side filtering
* Database indexes for commonly queried fields
* Efficient database queries
* Separation of API, business logic, and persistence

Microservices are intentionally not introduced because the current scale and domain do not justify their operational complexity.

## 11. Deployment

The frontend and backend will be independently deployable.

The deployed architecture will consist of:

```text
Angular Application
        |
        | REST API
        v
Spring Boot Application
        |
        v
PostgreSQL Database
```

Environment-specific configuration such as database credentials will be externalized and will not be committed to the repository.
