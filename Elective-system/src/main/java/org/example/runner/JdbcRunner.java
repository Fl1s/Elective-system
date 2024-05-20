package org.example.runner;

import org.example.entity.CourseEntity;
import org.example.service.CourseService;

import java.util.List;

public class JdbcRunner {
    private static CourseService courseService = new CourseService();

    public static void main(String[] args) {

        List<CourseEntity> courses = courseService.getAllCourses();
        System.out.println(courses);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseId(1);
        courseEntity.setCourseName("Computer Science");
        courseEntity.setTeacherId(1);

        CourseEntity savedCourse = courseService.addCourse(courseEntity);
        System.out.println(savedCourse);

        courseEntity.setCourseName("Updated Course Name");
        courseService.updateCourse(courseEntity);
        System.out.println("Course updated: " + courseEntity);

        CourseEntity course = courseService.getCourseById(1);
        System.out.println(course);

        courseService.deleteCourse(1);
        System.out.println("Course deleted with ID: 1");
    }
}
