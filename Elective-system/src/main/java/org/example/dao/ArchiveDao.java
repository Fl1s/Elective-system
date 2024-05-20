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
    private static final String CREATE_PSQL = """
            INSERT INTO archives(enrollment_id, grade, archive_date)
            VALUES(?,?,?)
            """;
    private static final String READ_PSQL = """
            SELECT archive_id,
                   enrollment_id,
                   grade,
                   archive_date
            FROM archives
            """;
    private static final String UPDATE_PSQL = """
            UPDATE archives
            SET enrollment_id = ?,
                grade = ?,
                archive_date = ?
            WHERE archive_id = ?
            """;
    private static final String DELETE_PSQL = """
            DELETE
            FROM archives
            WHERE archive_id = ?
            """;

    private ArchiveDao() {
    }

    public static ArchiveDao getInstance() {
        return INSTANSE;
    }

    public ArchiveEntity create(ArchiveEntity entity) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_PSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
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

    public List<ArchiveEntity> getAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_PSQL)) {

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

    public Optional<ArchiveEntity> getEntityById(ArchiveEntity id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(READ_PSQL)) {
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
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_PSQL)) {
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

    public boolean delete(int archive_id) {
        try (Connection conn = ConnectionManager.get();
        PreparedStatement preparedStatement = conn.prepareStatement(DELETE_PSQL)) {
            preparedStatement.setInt(1, archive_id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
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

