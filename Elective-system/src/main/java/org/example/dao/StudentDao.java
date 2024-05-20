package org.example.dao;

import org.example.entity.StudentEntity;
import org.example.exception.DaoException;
import org.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDao {
    private static final StudentDao INSTANCE = new StudentDao();

    private static final String CREATE_SQL = """
            INSERT INTO students(student_name, student_course, student_grade)
            VALUES(?, ?, ?)
            """;
    private static final String READ_SQL = """
            SELECT student_id, student_name, student_course, student_grade
            FROM students
            """;
    private static final String UPDATE_SQL = """
            UPDATE students
            SET student_name = ?, student_course = ?, student_grade = ?
            WHERE student_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE
            FROM students
            WHERE student_id = ?
            """;

    private StudentDao() {}

    public static StudentDao getInstance() {
        return INSTANCE;
    }

    public StudentEntity save(StudentEntity entity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getStudentName());
            preparedStatement.setString(2, entity.getStudentCourse());
            preparedStatement.setFloat(3, entity.getStudentGrade());

            preparedStatement.executeUpdate();

            ResultSet set = preparedStatement.getGeneratedKeys();
            if (set.next()) {
                entity.setStudentId(set.getInt("student_id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<StudentEntity> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL)) {

            ResultSet set = preparedStatement.executeQuery();
            List<StudentEntity> students = new ArrayList<>();

            while (set.next()) {
                students.add(buildStudent(set));
            }
            return students;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<StudentEntity> findById(int id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL + " WHERE student_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();

            StudentEntity studentEntity = null;
            if (set.next()) {
                studentEntity = buildStudent(set);
            }
            return Optional.ofNullable(studentEntity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public StudentEntity update(StudentEntity entity) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getStudentName());
            preparedStatement.setString(2, entity.getStudentCourse());
            preparedStatement.setFloat(3, entity.getStudentGrade());
            preparedStatement.setInt(4, entity.getStudentId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }

    public boolean delete(int studentId) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, studentId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static StudentEntity buildStudent(ResultSet set) throws SQLException {
        return new StudentEntity(
                set.getInt("student_id"),
                set.getString("student_name"),
                set.getString("student_course"),
                set.getFloat("student_grade")
        );
    }
}
