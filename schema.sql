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

-- Insert program
INSERT INTO basic_programs (name) VALUES ('HumTek');

-- Insert subject modules under HumTek
INSERT INTO subject_modules (basic_program_id, name)
VALUES
    ((SELECT id FROM basic_programs WHERE name='HumTek'), 'Computer Science'),
    ((SELECT id FROM basic_programs WHERE name='HumTek'), 'Informatics');

-- Insert basic courses for HumTek
INSERT INTO courses (name, is_basic, basic_program_id)
VALUES
    ('Basic Project 1', 1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('Basic Project 2', 1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('Basic Project 3', 1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('DK',              1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('STS',             1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('TSA',             1, (SELECT id FROM basic_programs WHERE name='HumTek')),
    ('Science Theory',  1, (SELECT id FROM basic_programs WHERE name='HumTek'));

-- Insert Computer Science courses
INSERT INTO courses (name, is_basic, subject_module_id)
VALUES
    ('Subject Module Project, Computer Science', 0, (SELECT id FROM subject_modules WHERE name='Computer Science')),
    ('Essential Computing',                      0, (SELECT id FROM subject_modules WHERE name='Computer Science')),
    ('Software Development',                     0, (SELECT id FROM subject_modules WHERE name='Computer Science')),
    ('Interactive Digital Systems',              0, (SELECT id FROM subject_modules WHERE name='Computer Science'));

-- Insert Informatics courses
INSERT INTO courses (name, is_basic, subject_module_id)
VALUES
    ('Subject Module Project, Informatics', 0, (SELECT id FROM subject_modules WHERE name='Informatics')),
    ('OFIT',                                0, (SELECT id FROM subject_modules WHERE name='Informatics')),
    ('BANDIT',                              0, (SELECT id FROM subject_modules WHERE name='Informatics')),
    ('WITS',                                0, (SELECT id FROM subject_modules WHERE name='Informatics'));