-- ============================================================
-- Lab 7 - Database setup for the JDBC Swing CRUD application
-- Run this ONCE in MySQL Workbench (or the mysql client) before
-- launching the app.
-- ============================================================

SHOW DATABASES;

-- 1) Create the database used by utility.DatabaseConnector
CREATE DATABASE IF NOT EXISTS customerdb;
USE customerdb;

-- 2) Create the table & columns the app reads/writes
CREATE TABLE IF NOT EXISTS student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(20),
    last_name  VARCHAR(20),
    college    VARCHAR(50),
    age        INT,
    hobbies    VARCHAR(100)
);

-- 3) (Optional) a couple of sample rows so the table view isn't empty
INSERT INTO student (first_name, age, college, hobbies)
VALUES ('Rish', 28, 'College of Engineering', 'Football, guitar');

INSERT INTO student (first_name, age, college, hobbies)
VALUES ('Ava', 22, 'Khoury College of Computer Sciences', 'Reading, chess');

-- ----- Handy reference queries --------------------------------------------

-- Read:
SELECT * FROM student;

-- Update (must use the primary key or MySQL safe-update mode blocks it):
-- UPDATE student SET college = 'Khoury College of Computer Sciences' WHERE student_id = 1;

-- Delete a single row:
-- DELETE FROM student WHERE student_id = 1;

-- Drop the whole table (clean slate):
-- DROP TABLE student;
