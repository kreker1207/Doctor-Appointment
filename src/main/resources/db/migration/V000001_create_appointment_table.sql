CREATE TABLE appointment(
    id SERIAL PRIMARY KEY ,
    schedule_id BIGINT NOT NULL ,
    start_time TIME NOT NULL ,
    end_time TIME NOT NULL,
    status VARCHAR(12),
    person_id BIGINT
);
