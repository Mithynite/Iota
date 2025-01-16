package iota.com.core;

import iota.com.utils.ReflectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database interactions and handles transactions for CRUD operations.
 */
public class EntityManager {
    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    // Transactions handling methods
    public void beginTransaction() throws SQLException {
        if (connection != null && connection.getAutoCommit()) {
            connection.setAutoCommit(false); // Begin a new transaction
        }
    }

    public void commitTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit(); // Commit the transaction
            connection.setAutoCommit(true); // Reset to default
        }
    }

    public void rollbackTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback(); // Roll back the transaction
            connection.setAutoCommit(true); // Reset to default
        }
    }

    // INSERT entity into database
    public <T> void persist(T entity) throws Exception {
        String sql = QueryBuilder.buildInsertQuery(entity);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Bind parameters, converting enums to strings if necessary
            QueryBuilder.bindParametersForInsert(stmt, entity);
            stmt.executeUpdate();
        }
    }

    // READ entity by ID
    public <T> T find(Class<T> clazz, Object id) throws Exception {
        String sql = QueryBuilder.buildSelectByIdQuery(clazz);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Map result set to entity, including enum handling
                    return (T) ReflectionUtils.mapResultSetToEntity(rs, clazz);
                }
            }
        }
        return null;
    }

    // DELETE entity by ID
    public <T> void delete(Class<T> clazz, Object id) throws Exception {
        String sql = QueryBuilder.buildDeleteByIdQuery(clazz);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    // UPDATE entity
    public <T> void update(T entity) throws Exception {
        String sql = QueryBuilder.buildUpdateQuery(entity);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Bind parameters, converting enums to strings if necessary
            QueryBuilder.bindParametersForUpdate(stmt, entity);
            stmt.executeUpdate();
        }
    }

    public <T> List<T> query(Class<T> clazz, String sql, List<Object> params) throws Exception {
        List<T> result = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Bind parameters to the prepared statement
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
            }

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                // Map each row of the result set to an entity object
                while (rs.next()) {
                    T entity = ReflectionUtils.mapResultSetToEntity(rs, clazz);
                    result.add(entity);
                }
            }
        }

        return result;
    }
}
