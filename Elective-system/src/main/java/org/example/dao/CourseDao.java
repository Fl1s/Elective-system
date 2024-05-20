package org.example.dao;

import org.example.entity.CourseEntity;
import org.example.exception.DaoException;
import org.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDao {
    private static final CourseDao INSTANCE = new CourseDao();

    private static final String CREATE_SQL = """
            INSERT INTO courses(course_name, teacher_id)
            VALUES(?, ?)
            """;
    private static final String READ_SQL = """
            SELECT course_id, course_name, teacher_id
            FROM courses
            """;
    private static final String UPDATE_SQL = """
            UPDATE courses
            SET course_name = ?, teacher_id = ?
            WHERE course_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE
            FROM courses
            WHERE course_id = ?
            """;

    private CourseDao() {
    }

    public static CourseDao getInstance() {
        return INSTANCE;
    }

    public CourseEntity save(CourseEntity entity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getCourseName());
            preparedStatement.setInt(2, entity.getTeacherId());

            preparedStatement.executeUpdate();

            ResultSet set = preparedStatement.getGeneratedKeys();
            if (set.next()) {
                entity.setCourseId(set.getInt("course_id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<CourseEntity> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL)) {

            ResultSet set = preparedStatement.executeQuery();
            List<CourseEntity> courses = new ArrayList<>();

            while (set.next()) {
                courses.add(buildCourse(set));
            }
            return courses;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<CourseEntity> findById(int id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL + " WHERE course_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();

            CourseEntity courseEntity = null;
            if (set.next()) {
                courseEntity = buildCourse(set);
            }
            return Optional.ofNullable(courseEntity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public CourseEntity update(CourseEntity entity) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getCourseName());
            preparedStatement.setInt(2, entity.getTeacherId());
            preparedStatement.setInt(3, entity.getCourseId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }

    public void delete(int courseId) throws DaoException {
        Connection connection = ConnectionManager.get();
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteArchives = connection.prepareStatement(
                    "DELETE FROM archives WHERE enrollment_id IN (SELECT enrollment_id FROM enrollments WHERE course_id = ?)")) {
                deleteArchives.setInt(1, courseId);
                deleteArchives.executeUpdate();
            }

            try (PreparedStatement deleteEnrollments = connection.prepareStatement("DELETE FROM enrollments WHERE course_id = ?")) {
                deleteEnrollments.setInt(1, courseId);
                deleteEnrollments.executeUpdate();
            }

            try (PreparedStatement deleteCourse = connection.prepareStatement(DELETE_SQL)) {
                deleteCourse.setInt(1, courseId);
                deleteCourse.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new DaoException(rollbackException);
            }
            throw new DaoException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitException) {
                throw new DaoException(autoCommitException);
            }
        }
    }


    private static CourseEntity buildCourse(ResultSet set) throws SQLException {
        return new CourseEntity(
                set.getInt("course_id"),
                set.getString("course_name"),
                set.getInt("teacher_id")
        );
    }
}
