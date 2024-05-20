package org.example.runner;

import org.example.entity.CourseEntity;
import org.example.service.CourseService;

import java.util.List;

public class JdbcRunner {
    private static CourseService courseService = new CourseService();

    public static void main(String[] args) {
        // Здесь пользователь может выполнять любые тесты

        // Пример: получить все курсы
        List<CourseEntity> courses = courseService.getAllCourses();
        System.out.println(courses);

        // Пример: сохранить новый курс
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseId(1);
        courseEntity.setCourseName("Computer Science");
        courseEntity.setTeacherId(1);

        CourseEntity savedCourse = courseService.addCourse(courseEntity);
        System.out.println(savedCourse);

        // Пример: обновить существующий курс
        courseEntity.setCourseName("Updated Course Name");
        courseService.updateCourse(courseEntity);
        System.out.println("Course updated: " + courseEntity);

        // Пример: получить курс по ID
        CourseEntity course = courseService.getCourseById(1);
        System.out.println(course);

        // Пример: удалить курс по ID
        courseService.deleteCourse(1);
        System.out.println("Course deleted with ID: 1");
    }
}
