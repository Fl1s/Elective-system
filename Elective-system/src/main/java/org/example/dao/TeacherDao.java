package org.example.dao;

import org.example.entity.TeacherEntity;
import org.example.exception.DaoException;
import org.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDao {
    private static final TeacherDao INSTANCE = new TeacherDao();

    private static final String CREATE_SQL = """
            INSERT INTO teachers(teacher_name)
            VALUES(?)
            """;
    private static final String READ_SQL = """
            SELECT teacher_id, teacher_name
            FROM teachers
            """;
    private static final String UPDATE_SQL = """
            UPDATE teachers
            SET teacher_name = ?
            WHERE teacher_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE
            FROM teachers
            WHERE teacher_id = ?
            """;

    private TeacherDao() {}

    public static TeacherDao getInstance() {
        return INSTANCE;
    }

    public TeacherEntity save(TeacherEntity entity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getTeacherName());

            preparedStatement.executeUpdate();

            ResultSet set = preparedStatement.getGeneratedKeys();
            if (set.next()) {
                entity.setTeacherId(set.getInt("teacher_id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<TeacherEntity> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL)) {

            ResultSet set = preparedStatement.executeQuery();
            List<TeacherEntity> teachers = new ArrayList<>();

            while (set.next()) {
                teachers.add(buildTeacher(set));
            }
            return teachers;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<TeacherEntity> findById(int id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL + " WHERE teacher_id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();

            TeacherEntity teacherEntity = null;
            if (set.next()) {
                teacherEntity = buildTeacher(set);
            }
            return Optional.ofNullable(teacherEntity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public TeacherEntity update(TeacherEntity entity) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, entity.getTeacherName());
            preparedStatement.setInt(2, entity.getTeacherId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return entity;
    }

    public boolean delete(int teacherId) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, teacherId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static TeacherEntity buildTeacher(ResultSet set) throws SQLException {
        return new TeacherEntity(
                set.getString("teacher_name"),
                set.getInt("teacher_id")
                );
    }
}
