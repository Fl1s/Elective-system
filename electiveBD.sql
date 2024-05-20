-- Definition of the teachers table
CREATE TABLE teachers
(
    teacher_id   int GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Unique identifier for teachers
    teacher_name varchar NOT NULL -- Name of the teacher
);

-- Definition of the students table
CREATE TABLE students
(
    student_id     int GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Unique identifier for students
    student_name   varchar NOT NULL, -- Name of the student
    student_course varchar NOT NULL, -- Course of the student
    student_grade  float -- Grade of the student
);

-- Definition of the courses table
CREATE TABLE courses
(
    course_id   int GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Unique identifier for courses
    course_name varchar NOT NULL, -- Name of the course
    teacher_id  INT, -- Identifier of the teacher for the course

    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) -- Foreign key reference to the teachers table
);

-- Definition of the enrollments table
CREATE TABLE enrollments
(
    enrollment_id   int GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Unique identifier for enrollments
    student_id      int, -- Identifier of the student
    course_id       int, -- Identifier of the course
    enrollment_date date NOT NULL DEFAULT CURRENT_DATE, -- Enrollment date for the course

    FOREIGN KEY (student_id) REFERENCES students (student_id), -- Foreign key reference to the students table
    FOREIGN KEY (course_id) REFERENCES courses (course_id), -- Foreign key reference to the courses table
    UNIQUE (student_id, course_id) -- Unique constraint for student-course combination
);

-- Definition of the archives table
CREATE TABLE archives
(
    archive_id    int GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Unique identifier for archives
    enrollment_id int, -- Identifier of the enrollment
    grade         float NOT NULL, -- Grade
    archive_date  date  NOT NULL DEFAULT CURRENT_DATE, -- Date of archiving

    FOREIGN KEY (enrollment_id) REFERENCES enrollments (enrollment_id) -- Foreign key reference to the enrollments table
);

-- Drop tables if they already exist (for testing purposes)
DROP TABLE IF EXISTS courses,students,teachers;

-- Insert data into the teachers table
INSERT INTO teachers (teacher_name) VALUES 
    ('John Smith'), ('Emily Johnson'), ('Michael Davis'), ('Sarah Thompson'), ('David Wilson'),
    ('Emma Martinez'), ('Christopher Lee'), ('Jessica Garcia'), ('Daniel Rodriguez'), ('Ashley Hernandez');

-- Insert data into the students table
INSERT INTO students (student_name, student_course, student_grade) VALUES 
    ('Alice Johnson', 'Math', 85.5), ('Bob Williams', 'History', 72.3), ('Charlie Brown', 'Science', 90.0),
    ('Olivia Taylor', 'Math', 78.6), ('James Moore', 'History', 65.2), ('Lily Anderson', 'Science', 88.9),
    ('William Clark', 'Math', 91.2), ('Sophia White', 'History', 75.8), ('Michael Thomas', 'Science', 83.4),
    ('Ella Scott', 'Math', 87.1);

-- Insert data into the courses table
INSERT INTO courses (course_name, teacher_id) VALUES 
    ('Math', 1), ('History', 2), ('Science', 3), ('English', 4), ('Art', 5),
    ('Music', 6), ('Physics', 7), ('Chemistry', 8), ('Biology', 9), ('Computer Science', 10);

-- Insert data into the enrollments table
INSERT INTO enrollments (student_id, course_id) VALUES 
    (1, 1), (2, 2), (3, 3), (4, 4), (5, 5),
    (6, 6), (7, 7), (8, 8), (9, 9), (10, 10);

-- Insert data into the archives table
INSERT INTO archives (enrollment_id, grade) VALUES 
    (1, 85.5), (2, 72.3), (3, 90.0), (4, 78.6), (5, 65.2),
    (6, 88.9), (7, 91.2), (8, 75.8), (9, 83.4), (10, 87.1);
