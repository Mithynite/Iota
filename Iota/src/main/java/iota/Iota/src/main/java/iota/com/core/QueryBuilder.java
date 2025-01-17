package iota.com.core;

import iota.com.annotations.Column;
import iota.com.annotations.Table;
import iota.com.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.StringJoiner;

/***
 * Dynamically generates SQL queries.
 */
public class QueryBuilder {

    /**
 * Builds an INSERT SQL query based on the given entity object.
 * The method retrieves the table name and column names from the entity's annotations,
 * and constructs the INSERT query string with placeholders for the values.
 *
 * @param <T> The type of the entity.
 * @param entity The entity object containing the data to be inserted.
 * @return A String representing the INSERT SQL query.
 * @throws Exception If any error occurs during reflection or annotation processing.
 */
public static <T> String buildInsertQuery(T entity) throws Exception {
    Class<?> clazz = entity.getClass();
    Table table = ReflectionUtils.getTableAnnotation(clazz);

    StringJoiner columnNames = new StringJoiner(", ");
    StringJoiner placeholders = new StringJoiner(", ");
    for (Field field : clazz.getDeclaredFields()) {
        Column column = ReflectionUtils.getColumnAnnotation(field);
        if (column != null) {
            columnNames.add(column.name());
            placeholders.add("?");
        }
    }
    return "INSERT INTO " + table.name() + " (" + columnNames + ") VALUES (" + placeholders + ")";
}

    /**
 * Builds a SELECT SQL query based on the given entity class, targeting a specific record by its ID.
 *
 * @param <T> The type of the entity.
 * @param clazz The class of the entity for which the SELECT query is being built.
 * @return A SELECT SQL query string targeting a specific record by its ID.
 * @throws Exception If any error occurs during reflection or annotation processing.
 */
public static <T> String buildSelectByIdQuery(Class<T> clazz) throws Exception {
    Table table = ReflectionUtils.getTableAnnotation(clazz);
    Field idField = ReflectionUtils.getIdField(clazz);
    Column idColumn = ReflectionUtils.getColumnAnnotation(idField);

    return "SELECT * FROM " + table.name() + " WHERE " + idColumn.name() + " = ?";
}

    // Build DELETE query by ID
    public static <T> String buildDeleteByIdQuery(Class<T> clazz) throws Exception {
        Table table = ReflectionUtils.getTableAnnotation(clazz);
        Field idField = ReflectionUtils.getIdField(clazz);
        Column idColumn = ReflectionUtils.getColumnAnnotation(idField);

        return "DELETE FROM " + table.name() + " WHERE " + idColumn.name() + " = ?";
    }

    /**
 * Builds an UPDATE SQL query for the given entity.
 *
 * @param <T> The type of the entity.
 * @param entity The entity object to be updated.
 * @return The UPDATE SQL query string.
 * @throws Exception If any error occurs during reflection or annotation processing.
 */
public static <T> String buildUpdateQuery(T entity) throws Exception {
    Class<?> clazz = entity.getClass();
    Table table = ReflectionUtils.getTableAnnotation(clazz);
    Field idField = ReflectionUtils.getIdField(clazz);
    Column idColumn = ReflectionUtils.getColumnAnnotation(idField);

    StringJoiner setClauses = new StringJoiner(", ");
    for (Field field : clazz.getDeclaredFields()) {
        Column column = ReflectionUtils.getColumnAnnotation(field);
        if (column != null && !field.equals(idField)) {
            setClauses.add(column.name() + " = ?");
        }
    }
    return "UPDATE " + table.name() + " SET " + setClauses + " WHERE " + idColumn.name() + " = ?";
}

    /**
 * Binds the field values of the given entity to the PreparedStatement for an INSERT operation.
 *
 * @param <T> The type of the entity.
 * @param stmt The PreparedStatement to bind the values to.
 * @param entity The entity object containing the field values.
 * @throws Exception If any error occurs during reflection or PreparedStatement binding.
 */
public static <T> void bindParametersForInsert(PreparedStatement stmt, T entity) throws Exception {
    List<Object> values = ReflectionUtils.getFieldValues(entity);
    for (int i = 0; i < values.size(); i++) {
        stmt.setObject(i + 1, values.get(i));
    }
}

    /**
 * Binds the field values of the given entity to the PreparedStatement for an UPDATE operation.
 * The method retrieves the field values excluding the ID field, adds the ID value at the end,
 * and then binds these values to the PreparedStatement using the setObject method.
 *
 * @param <T> The type of the entity.
 * @param stmt The PreparedStatement to bind the values to.
 * @param entity The entity object containing the field values.
 * @throws Exception If any error occurs during reflection or PreparedStatement binding.
 */
public static <T> void bindParametersForUpdate(PreparedStatement stmt, T entity) throws Exception {
    List<Object> values = ReflectionUtils.getFieldValuesWithoutId(entity);
    Field idField = ReflectionUtils.getIdField(entity.getClass());
    values.add(ReflectionUtils.getFieldValue(entity, idField));

    for (int i = 0; i < values.size(); i++) {
        stmt.setObject(i + 1, values.get(i));
    }
}
}
