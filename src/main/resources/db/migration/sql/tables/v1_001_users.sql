CREATE TABLE if not exists olvan_users (
                       id bigint generated by default as identity primary key,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       parents_full_name VARCHAR(255),
                       pupil_phone_number VARCHAR(255),
                       parents_phone_number VARCHAR(255),
                       enroll_type VARCHAR(255),
                       date_begin VARCHAR(255),
                       inn VARCHAR(9),
                       course_type TEXT,
                       degree VARCHAR(255),
                       gender VARCHAR(255),
                       email VARCHAR(255),
                       experience DECIMAL(10, 2),
                       student_count INT,
                       attendance INT,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       role VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL
);
