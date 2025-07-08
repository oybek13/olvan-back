ALTER TABLE olvan_students
    ADD COLUMN client_id BIGINT,
ADD CONSTRAINT fk_student_client FOREIGN KEY (client_id) REFERENCES olvan_clients(id);
