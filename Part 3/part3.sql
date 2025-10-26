-- Part 2

-- Mads Degn, Daniel Holst Pedersen
-- 28/10-25

-- Enable foreign key constraints
PRAGMA foreign_keys = ON;

-- Drop tables for clean re-run during development
DROP TABLE IF EXISTS selections;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS subject_modules;
DROP TABLE IF EXISTS basic_programs;

-- Table of basic programs
CREATE TABLE basic_programs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

-- Table of subject modules
CREATE TABLE subject_modules (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    basic_program_id INTEGER NOT NULL,
    name TEXT NOT NULL UNIQUE,
    FOREIGN KEY (basic_program_id) REFERENCES basic_programs(id)
);

-- Table of students
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    basic_program_id INTEGER,
    subject1_id INTEGER,
    subject2_id INTEGER,
    FOREIGN KEY (basic_program_id) REFERENCES basic_programs(id),
    FOREIGN KEY (subject1_id) REFERENCES subject_modules(id),
    FOREIGN KEY (subject2_id) REFERENCES subject_modules(id)
);

-- Table of courses (both basic and subject-module)
CREATE TABLE courses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    is_basic INTEGER NOT NULL CHECK (is_basic IN (0, 1)),
    basic_program_id INTEGER,
    subject_module_id INTEGER,
    FOREIGN KEY (basic_program_id) REFERENCES basic_programs(id),
    FOREIGN KEY (subject_module_id) REFERENCES subject_modules(id)
);

-- Linking table: each row = one student choosing one course
CREATE TABLE selections (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- Insert data
-- Enable foreign key constraints
PRAGMA foreign_keys = ON;

-- Make the HumTek program
INSERT INTO basic_programs(name) VALUES ('HumTek');

-- Creates 2 subject modules under HumTek
INSERT INTO subject_modules(basic_program_id, name)
VALUES
    ((SELECT id FROM basic_programs WHERE name='HumTek'), 'Informatics'),
    ((SELECT id FROM basic_programs WHERE name='HumTek'), 'Computer Science');

-- Insert basic courses, based on the selected basic program
INSERT INTO basic_courses(name, is_basic, basic_program_id) VALUES
    ('Basic Project 1', 1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('Basic Project 2', 1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('Basic Project 3', 1, (SELECT id FROM basic_programs WHERE name='HumTek')),    
    ('TSA',             1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('DK',              1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('STS',             1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('Science Theory',  1, (SELECT id FROM basic_programs WHERE name='HumTek'));

-- Creates 2 subject modules
INSERT INTO basic_courses(name, is_basic, subject_module_id) VALUES
    ('Essential Computing',  0, (SELECT id FROM subject_modules WHERE name='Computer Science')),
    ('WITS',                 0, (SELECT id FROM subject_modules WHERE name='Informatics'));

-- Create a student "Brian" who attends HumTek and has the 2 subject modules
INSERT INTO students(name, basic_program_id, subject1_id, subject2_id)
SELECT
    'Brian' AS name,
    p.id AS basic_program_id,
    sm1.id AS subject1_id,
    sm2.id AS subject2_id
FROM basic_programs p
         JOIN (SELECT id FROM subject_modules WHERE name='Computer Science') AS sm1
         JOIN (SELECT id FROM subject_modules WHERE name='Informatics') AS sm2
WHERE p.name='HumTek';

-- Brian chooses 5 basic courses. In case the script is run again, "UNIQUE" avoids it being inserted twice
INSERT INTO selections(student_id, basic_course_id)
SELECT s.id, bc.id
FROM students s, basic_courses bc
WHERE s.name='Brian' AND bc.name IN ('Basic Project 2','TSA','STS', 'Science Theory', 'DK');

-- Create a student "Laura" who attends HumTek
INSERT INTO students(name, basic_program_id, subject1_id, subject2_id)
SELECT
    'Laura' AS name,
    p.id AS basic_program_id,
    sm1.id AS subject1_id,
    sm2.id AS subject2_id
FROM basic_programs p
         JOIN (SELECT id FROM subject_modules WHERE name='Computer Science') AS sm1
         JOIN (SELECT id FROM subject_modules WHERE name='Informatics') AS sm2
WHERE p.name='HumTek';

-- Laura chooses 3 basic courses. In case the script is run again, "UNIQUE" avoids it being inserted twice
INSERT INTO selections(student_id, basic_course_id)
SELECT s.id, bc.id
FROM students s, basic_courses bc
WHERE s.name='Laura' AND bc.name IN ('Basic Project 1','BANDIT','OFIT');

-- Create a student "Emil" who attends HumTek
INSERT INTO students(name, basic_program_id, subject1_id, subject2_id)
SELECT
    'Emil' AS name,
    p.id AS basic_program_id,
    sm1.id AS subject1_id,
    sm2.id AS subject2_id
FROM basic_programs p
         JOIN (SELECT id FROM subject_modules WHERE name='Computer Science') AS sm1
         JOIN (SELECT id FROM subject_modules WHERE name='Informatics') AS sm2
WHERE p.name='HumTek';

-- Emil chooses 3 basic courses. In case the script is run again, "UNIQUE" avoids it being inserted twice
INSERT INTO selections(student_id, basic_course_id)
SELECT s.id, bc.id
FROM students s, basic_courses bc
WHERE s.name='Emil' AND bc.name IN ('Basic Project 3','Software Development','Interactive Digital Systems');