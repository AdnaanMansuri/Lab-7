-- ============================================================
-- Lab 7 - Database setup for the JDBC Swing CRUD application
-- Run this ONCE in MySQL Workbench (or the mysql client) before
-- launching the app.
-- ============================================================

SHOW DATABASES;

-- 1) Create the database used by utility.DatabaseConnector
CREATE DATABASE IF NOT EXISTS customerdb;
USE customerdb;

-- 2) Create the table & columns the app reads/writes.
--    NOTE: this lab adds many new fields (gender, dob, phone, email,
--    experience, photo, ...). If you ran an EARLIER version of this lab,
--    the old "student" table has fewer columns, so drop it first.
DROP TABLE IF EXISTS student;

CREATE TABLE student (
    student_id  INT PRIMARY KEY AUTO_INCREMENT,
    first_name  VARCHAR(50),
    last_name   VARCHAR(50),
    gender      VARCHAR(10),
    dob         DATE,
    age         INT,
    phone       VARCHAR(20),
    email       VARCHAR(100),
    college     VARCHAR(100),
    experience  VARCHAR(255),
    hobbies     VARCHAR(255),
    photo       LONGBLOB            -- raw image bytes (the student photo)
);

-- 3) (Optional) a couple of sample rows so the table view isn't empty.
--    Photos are left NULL here; upload a photo from the app's form instead.
INSERT INTO student (first_name, last_name, gender, dob, age, phone, email, college, experience, hobbies)
VALUES ('Rish', 'Kaushick', 'Male', '1997-04-12', 28, '617-555-0142',
        'rish@northeastern.edu', 'College of Engineering', '3 years', 'Football, guitar');

INSERT INTO student (first_name, last_name, gender, dob, age, phone, email, college, experience, hobbies)
VALUES ('Ava', 'Patel', 'Female', '2003-09-30', 22, '857-555-0199',
        'ava@northeastern.edu', 'Khoury College of Computer Sciences', '1 year', 'Reading, chess');

-- ----- Handy reference queries --------------------------------------------

-- Read:
SELECT student_id, first_name, last_name, gender, dob, age, phone, email,
       college, experience, hobbies FROM student;

-- Update (must use the primary key or MySQL safe-update mode blocks it):
-- UPDATE student SET college = 'Khoury College of Computer Sciences' WHERE student_id = 1;

-- Delete a single row:
-- DELETE FROM student WHERE student_id = 1;

-- Drop the whole table (clean slate):
-- DROP TABLE student;
