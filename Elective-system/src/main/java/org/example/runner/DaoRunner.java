package org.example.runner;

import org.example.dao.StudentDao;
import org.example.entity.StudentEntity;

import java.util.List;
import java.util.Optional;

public class DaoRunner {

    public static void main(String[] args) {
        findAllTest();
        saveTest();
        updateTest();
        testSelect(1);
        deleteTest(1);
    }

    private static void findAllTest() {
        StudentDao studentDao = StudentDao.getInstance();
        List<StudentEntity> students = studentDao.findAll();
        System.out.println(students);
    }

    private static void updateTest() {
        StudentDao studentDao = StudentDao.getInstance();
        Optional<StudentEntity> maybeStudent = studentDao.findById(1);
        System.out.println(maybeStudent);

        maybeStudent.ifPresent(studentEntity -> {
            studentEntity.setStudentName("Updated Name");
            studentDao.update(studentEntity);
        });

        System.out.println(maybeStudent);
    }

    private static void testSelect(int studentId) {
        StudentDao studentDao = StudentDao.getInstance();
        Optional<StudentEntity> student = studentDao.findById(studentId);
        System.out.println(student);
    }

    private static void deleteTest(int studentId) {
        StudentDao studentDao = StudentDao.getInstance();
        studentDao.delete(studentId);
    }

    private static void saveTest() {
        StudentDao studentDao = StudentDao.getInstance();
        StudentEntity studentEntity = new StudentEntity();

        studentEntity.setStudentId(1);
        studentEntity.setStudentName("John Doe");
        studentEntity.setStudentCourse("Computer Science");

        StudentEntity savedStudent = studentDao.save(studentEntity);
        System.out.println(savedStudent);
    }
}
