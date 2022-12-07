CREATE TABLE schedule(
    id SERIAL PRIMARY KEY,
    doctor_id BIGINT,
    date timestamp NOT NULL
);