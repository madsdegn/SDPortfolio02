CREATE TABLE programs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE subject_modules (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    program_id INTEGER,
    name TEXT NOT NULL,
    FOREIGN KEY(program_id) REFERENCES programs(id)
);

CREATE TABLE courses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    module_id INTEGER,
    name TEXT NOT NULL,
    FOREIGN KEY(module_id) REFERENCES subject_modules(id)
);

CREATE TABLE selections (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_name TEXT,
    program TEXT,
    subject1 TEXT,
    subject2 TEXT,
    course_basic TEXT,
    course_subject1 TEXT,
    course_subject2 TEXT
);

INSERT INTO programs (name) VALUES 
('Software Development'), 
('Web Development'), 
('IT Security');

INSERT INTO subject_modules (program_id, name) VALUES
(1, '1st Semester'),
(1, '2nd Semester'),
(2, 'Frontend Basics'),
(3, 'Network Security');

INSERT INTO courses (module_id, name) VALUES
(1, 'Java Basics'),
(1, 'Databases'),
(2, 'Algorithms'),
(3, 'HTML & CSS'),
(4, 'Cryptography');