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

    /**
     * Constructs an EntityManager instance with the given database connection.
     *
     * @param connection The database connection to use for managing transactions and CRUD operations.
     */
    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    // Transactions handling methods

    /**
     * Begins a new database transaction.
     *
     * @throws SQLException If an error occurs while beginning the transaction.
     */
    public void beginTransaction() throws SQLException {
        if (connection != null && connection.getAutoCommit()) {
            connection.setAutoCommit(false); // Begin a new transaction
        }
    }

    /**
     * Commits the current database transaction.
     *
     * @throws SQLException If an error occurs while committing the transaction.
     */
    public void commitTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit(); // Commit the transaction
            connection.setAutoCommit(true); // Reset to default
        }
    }

    /**
     * Rolls back the current database transaction.
     *
     * @throws SQLException If an error occurs while rolling back the transaction.
     */
    public void rollbackTransaction() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback(); // Roll back the transaction
            connection.setAutoCommit(true); // Reset to default
        }
    }

    // CRUD operations

    /**
     * Inserts the given entity into the database.
     *
     * @param entity The entity to insert.
     * @throws Exception If an error occurs while inserting the entity.
     */
    public <T> void persist(T entity) throws Exception {
        String sql = QueryBuilder.buildInsertQuery(entity);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Bind parameters, converting enums to strings if necessary
            QueryBuilder.bindParametersForInsert(stmt, entity);
            stmt.executeUpdate();
        }
    }

    /**
     * Finds and returns the entity of the given class with the specified ID from the database.
     *
     * @param clazz The class of the entity to find.
     * @param id    The ID of the entity to find.
     * @return The found entity, or null if no entity with the specified ID exists.
     * @throws Exception If an error occurs while finding the entity.
     */
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

    /**
     * Deletes the entity of the given class with the specified ID from the database.
     *
     * @param clazz The class of the entity to delete.
     * @param id    The ID of the entity to delete.
     * @throws Exception If an error occurs while deleting the entity.
     */
    public <T> void delete(Class<T> clazz, Object id) throws Exception {
        String sql = QueryBuilder.buildDeleteByIdQuery(clazz);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Updates the given entity in the database.
     *
     * @param entity The entity to update.
     * @throws Exception If an error occurs while updating the entity.
     */
    public <T> void update(T entity) throws Exception {
        String sql = QueryBuilder.buildUpdateQuery(entity);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Bind parameters, converting enums to strings if necessary
            QueryBuilder.bindParametersForUpdate(stmt, entity);
            stmt.executeUpdate();
        }
    }

    /**
     * Executes the given SQL query with the specified parameters and returns a list of entities of the given class.
     *
     * @param clazz The class of the entities to return.
     * @param sql   The SQL query to execute.
     * @param params The parameters to bind to the SQL query.
     * @return A list of entities of the given class.
     * @throws Exception If an error occurs while executing the query or mapping the result set to entities.
     */
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
