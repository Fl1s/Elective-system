package org.example.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.entity.ArchiveEntity;
import org.example.exception.DaoException;
import org.util.ConnectionManager;

public class ArchiveDao {
    private static final ArchiveDao INSTANSE = new ArchiveDao();
    private static final String CREATE_SQL = """
            INSERT INTO archives(enrollment_id, grade, archive_date)
            VALUES(?,?,?)
            """;
    private static final String READ_SQL = """
            SELECT archive_id,
                   enrollment_id,
                   grade,
                   archive_date
            FROM archives
            """;
    private static final String UPDATE_SQL = """
            UPDATE archives
            SET enrollment_id = ?,
                grade = ?,
                archive_date = ?
            WHERE archive_id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE
            FROM archives
            WHERE archive_id = ?
            """;

    private ArchiveDao() {
    }

    public static ArchiveDao getInstance() {
        return INSTANSE;
    }

    public ArchiveEntity save(ArchiveEntity entity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, entity.getArchiveId());
            preparedStatement.setInt(2, entity.getEnrollmentId());
            preparedStatement.setDouble(3, entity.getGrade());

            preparedStatement.executeUpdate();

            ResultSet set = preparedStatement.getGeneratedKeys();
            if (set.next()) {
                entity.setArchiveId(set.getInt("archive_id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<ArchiveEntity> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL)) {

            ResultSet set = preparedStatement.executeQuery();
            List<ArchiveEntity> archives = new ArrayList<>();

            while (set.next()) {
                archives.add(buildArchive(set));
            }
            return archives;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<ArchiveEntity> findById(ArchiveEntity id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_SQL)) {
            preparedStatement.setObject(1, id);
            ResultSet set = preparedStatement.executeQuery();

            ArchiveEntity archiveEntity = null;
            while (set.next()) {
                archiveEntity = buildArchive(set);
            }
            return Optional.ofNullable(archiveEntity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public ArchiveEntity update(ArchiveEntity entity) {
        try (Connection conn = ConnectionManager.get();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, entity.getEnrollmentId());
            preparedStatement.setDouble(2, entity.getGrade());
            preparedStatement.setDate(3, entity.getArchiveDate());
            preparedStatement.setInt(4, entity.getArchiveId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
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


    private static ArchiveEntity buildArchive(ResultSet set) throws SQLException {
        return new ArchiveEntity(
                set.getInt("archive_id"),
                set.getInt("enrollment_id"),
                set.getDouble("grade"),
                set.getDate("archive_data")
        );
    }
}

