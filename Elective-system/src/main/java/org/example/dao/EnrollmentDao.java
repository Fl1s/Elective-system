package org.example.dao;

import org.example.entity.EnrollmentEntity;
import org.example.exception.DaoException;
import org.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentDao {
    private static final EnrollmentDao INSTANCE = new EnrollmentDao();

    private static final String CREATE_SQL = """
            INSERT INTO enrollments(student_id, course_id, enrollment_date)
            VALUES(?, ?, ?)
            """;
    private static final String READ_SQL = """
            SELECT enrollment_id, student_id, course_id, enrollment_date
            FROM enrollments
            """;
    private static final String UPDATE_SQL = """
            UPDATE enrollments
            SET student_id = ?, course_id = ?, enrollment_date = ?
            WHERE enrollment_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE
            FROM enrollments
            WHERE enrollment_id = ?
            """;

    private EnrollmentDao() {}

    public static EnrollmentDao getInstance() {
        return INSTANCE;
    }

    public EnrollmentEntity save(EnrollmentEntity entity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getStudentId());
            preparedStatement.setInt(2, entity.getCourseId());
            preparedStatement.setDate(3, entity.getEnrollmentDate());

            preparedStatement.executeUpdate();

            ResultSet set = preparedStatement.getGeneratedKeys();
            if (set.next()) {
                entity.setEnrollmentId(set.getInt("enrollment_id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<EnrollmentEntity> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL)) {

            ResultSet set = preparedStatement.executeQuery();
            List<EnrollmentEntity> enrollments = new ArrayList<>();

            while (set.next()) {
                enrollments.add(buildEnrollment(set));
            }
            return enrollments;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<EnrollmentEntity> findById(int id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL + " WHERE enrollment_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();

            EnrollmentEntity enrollmentEntity = null;
            if (set.next()) {
                enrollmentEntity = buildEnrollment(set);
            }
            return Optional.ofNullable(enrollmentEntity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public EnrollmentEntity update(EnrollmentEntity entity) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, entity.getStudentId());
            preparedStatement.setInt(2, entity.getCourseId());
            preparedStatement.setDate(3, entity.getEnrollmentDate());
            preparedStatement.setInt(4, entity.getEnrollmentId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }

    public boolean delete(int enrollmentId) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, enrollmentId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static EnrollmentEntity buildEnrollment(ResultSet set) throws SQLException {
        return new EnrollmentEntity(
                set.getInt("enrollment_id"),
                set.getInt("student_id"),
                set.getInt("course_id"),
                set.getDate("enrollment_date").toLocalDate()
        );
    }
}
