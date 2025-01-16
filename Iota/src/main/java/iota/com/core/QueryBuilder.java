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

    // Build INSERT query
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

    // Build SELECT query by ID
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

    // Build UPDATE query
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

    // Bind parameters for INSERT query
    public static <T> void bindParametersForInsert(PreparedStatement stmt, T entity) throws Exception {
        List<Object> values = ReflectionUtils.getFieldValues(entity);
        for (int i = 0; i < values.size(); i++) {
            stmt.setObject(i + 1, values.get(i));
        }
    }

    // Bind parameters for UPDATE query
    public static <T> void bindParametersForUpdate(PreparedStatement stmt, T entity) throws Exception {
        List<Object> values = ReflectionUtils.getFieldValuesWithoutId(entity);
        Field idField = ReflectionUtils.getIdField(entity.getClass());
        values.add(ReflectionUtils.getFieldValue(entity, idField));

        for (int i = 0; i < values.size(); i++) {
            stmt.setObject(i + 1, values.get(i));
        }
    }
}
