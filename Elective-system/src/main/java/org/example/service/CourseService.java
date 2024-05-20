package org.example.service;

import org.example.dao.CourseDao;
import org.example.dao.EnrollmentDao;
import org.example.dao.ArchiveDao;
import org.example.entity.CourseEntity;
import org.example.entity.EnrollmentEntity;
import org.example.entity.ArchiveEntity;

import java.sql.Date;
import java.util.List;

public class CourseService {
    private final CourseDao courseDao = CourseDao.getInstance();
    private final EnrollmentDao EnrollmentDao = org.example.dao.EnrollmentDao.getInstance();
    private final ArchiveDao archiveDao = ArchiveDao.getInstance();

    public void announceCourse(CourseEntity course) {
        courseDao.save(course);
    }

    public CourseEntity addCourse(CourseEntity course) {
        return courseDao.save(course);
    }

    public CourseEntity getCourseById(int id) {
        return courseDao.findById(id).orElse(null);
    }

    public void updateCourse(CourseEntity course) {
        courseDao.update(course);
    }

    public void deleteCourse(int id) {
        courseDao.delete(id);
    }

    public void enrollStudent(int studentId, int courseId) {
        EnrollmentEntity enrollment = new EnrollmentEntity();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(new Date(System.currentTimeMillis()).toLocalDate());

        EnrollmentDao.save(enrollment);
    }

    public void gradeStudent(int enrollmentId, double grade) {
        ArchiveEntity archive = new ArchiveEntity();
        archive.setEnrollmentId(enrollmentId);
        archive.setGrade(grade);
        archive.setArchiveDate(new java.sql.Date(System.currentTimeMillis()));

        archiveDao.save(archive);
    }

    public List<CourseEntity> getAllCourses() {
        return courseDao.findAll();
    }

    public List<EnrollmentEntity> getAllEnrollments() {
        return EnrollmentDao.findAll();
    }
}
