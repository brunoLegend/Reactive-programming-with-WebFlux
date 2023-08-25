DROP TABLE IF EXISTS students;
CREATE TABLE students(
     id SERIAL PRIMARY KEY,
     name VARCHAR(255),
     birthday VARCHAR(255),
     credits INT,
     avg_grade FLOAT
);

DROP TABLE IF EXISTS professors;
CREATE TABLE professors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

DROP TABLE IF EXISTS relationships;
CREATE TABLE relationships  (
    id SERIAL PRIMARY KEY,
    s_id BIGINT NOT NULL,
    p_id BIGINT NOT NULL,
    UNIQUE (s_id, p_id)
);