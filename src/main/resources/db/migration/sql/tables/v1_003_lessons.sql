CREATE TABLE if not exists olvan_lessons (
                               id bigint generated by default as identity primary key,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               org_id BIGINT NOT NULL,
                               name VARCHAR(255) NOT NULL,
                               description TEXT NOT NULL,
                               age_category VARCHAR(255) NOT NULL,
                               pupil_count INT NOT NULL,
                               price BIGINT NOT NULL,
                               teacher_full_name VARCHAR(255) NOT NULL,
                               status BOOLEAN NOT NULL,
                               date_begin VARCHAR(255) NOT NULL,
                               days VARCHAR(255) NOT NULL,
                               time_begin VARCHAR(255) NOT NULL,
                               time_end VARCHAR(255) NOT NULL
);
