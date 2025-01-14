package iota.com.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/***
 * Performs CRUD operations (save, update, delete, findById, findAll) for entity classes.
 * Uses reflection (via ReflectionUtils) to map an object's fields and annotations (e.g., @Column) to database tables.
 */
public class EntityManager {
    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
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

    /**
     * Query method for retrieving a list of entities based on a custom query.
     *
     * @param clazz    The entity class
     * @param sql      The SQL query string
     * @param params   A list of parameters to bind to the query
     * @param <T>      The entity type
     * @return List of entities matching the query
     * @throws Exception If any error occurs
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
