INSERT INTO employees (
    employee_code,
    first_name,
    last_name,
    email,
    country,
    department,
    job_title,
    employment_status,
    created_at,
    updated_at
)
SELECT
    'SEED' || LPAD(i::TEXT, 5, '0'),
    'Employee' || i,
    'Staff' || i,
    'seed.employee' || LPAD(i::TEXT, 5, '0') || '@example.com',
    CASE i % 10
        WHEN 0 THEN 'IN'
        WHEN 1 THEN 'US'
        WHEN 2 THEN 'GB'
        WHEN 3 THEN 'CA'
        WHEN 4 THEN 'DE'
        WHEN 5 THEN 'AU'
        WHEN 6 THEN 'SG'
        WHEN 7 THEN 'AE'
        WHEN 8 THEN 'FR'
        ELSE 'JP'
    END,
    CASE i % 6
        WHEN 0 THEN 'Engineering'
        WHEN 1 THEN 'Human Resources'
        WHEN 2 THEN 'Finance'
        WHEN 3 THEN 'Sales'
        WHEN 4 THEN 'Operations'
        ELSE 'IT'
    END,
    CASE i % 5
        WHEN 0 THEN 'Software Engineer'
        WHEN 1 THEN 'HR Specialist'
        WHEN 2 THEN 'Financial Analyst'
        WHEN 3 THEN 'Sales Executive'
        ELSE 'Operations Manager'
    END,
    CASE WHEN i % 20 = 0 THEN 'INACTIVE' ELSE 'ACTIVE' END,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM GENERATE_SERIES(1, 10000) AS series(i)
ON CONFLICT DO NOTHING;