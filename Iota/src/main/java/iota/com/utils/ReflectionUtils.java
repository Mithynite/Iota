package iota.com.utils;

import iota.com.annotations.Column;
import iota.com.annotations.Id;
import iota.com.annotations.Table;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Maps an object's fields and annotations (e.g., @Column) to database tables.
 * Also handles mapping Enums between database values and Java Enum types.
 */
public class ReflectionUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Get the @Table annotation from a class
    public static Table getTableAnnotation(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class);
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @Table");
    }

    // Get the @Column annotation from a field
    public static Column getColumnAnnotation(Field field) {
        return field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class) : null;
    }

    // Get the field annotated with @Id from a class
    public static Field getIdField(Class<?> clazz) throws NoSuchFieldException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new NoSuchFieldException("No field with @Id annotation found in " + clazz.getName());
    }

    // Get the value of the field annotated with @Id
    public static Object getIdValue(Object entity) throws Exception {
        Field idField = getIdField(entity.getClass());
        idField.setAccessible(true); // Allow access to private fields
        return idField.get(entity);
    }

    // Get all values from fields of an entity (for INSERT)
    public static List<Object> getFieldValues(Object entity) throws IllegalAccessException {
        List<Object> values = new ArrayList<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            Column column = getColumnAnnotation(field);
            if (column != null) {
                field.setAccessible(true);
                Object value = field.get(entity);

                // Handle Enum fields (convert to String for database storage)
                if (field.getType().isEnum() && value != null) {
                    value = ((Enum<?>) value).name(); // Convert Enum to String (e.g., Gender.MALE -> "MALE")
                }

                values.add(value);
            }
        }
        return values;
    }

    // Get all field values except the one annotated with @Id (for UPDATE)
    public static List<Object> getFieldValuesWithoutId(Object entity) throws Exception {
        List<Object> values = new ArrayList<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            Column column = getColumnAnnotation(field);
            if (column != null && !field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                Object value = field.get(entity);

                // Handle Enum fields (convert to String for database storage)
                if (field.getType().isEnum() && value != null) {
                    value = ((Enum<?>) value).name();
                }

                values.add(value);
            }
        }
        return values;
    }

    // Get the value of a specific field by name
    public static Object getFieldValue(Object entity, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(entity);
    }

    // Map data from a ResultSet to an entity object
    public static <T> T mapResultSetToEntity(ResultSet rs, Class<T> clazz) throws Exception {
        T entity = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            Column column = getColumnAnnotation(field);
            if (column != null) {
                Object value = rs.getObject(column.name());

                // Handle Enum fields (convert from String to Enum for Java field)
                if (field.getType().isEnum() && value != null) {
                    value = Enum.valueOf((Class<Enum>) field.getType(), value.toString()); // Convert String to Enum
                }

                field.setAccessible(true);
                field.set(entity, value);
            }
        }
        return entity;
    }

    // Update the convertValue method
    private static Object convertValue(Class<?> type, String value) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(value);
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Date.class) {
            try {
                return DATE_FORMAT.parse(value); // Parse date format
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format for value: " + value);
            }
        } else if (type.isEnum()) {
            // Convert CSV string to Enum constant
            return Enum.valueOf((Class<Enum>) type, value.toLowerCase());
        } else if (type == String.class) {
            return value;
        }
        throw new IllegalArgumentException("Unsupported field type: " + type);
    }


    public static <T> T mapCsvRowToEntity(Class<T> clazz, List<String> fieldMapping, String[] csvRowData) throws Exception {
        T entity = clazz.getDeclaredConstructor().newInstance();

        for (int i = 0; i < fieldMapping.size(); i++) {
            String fieldName = fieldMapping.get(i);
            String value = csvRowData[i].trim();

            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            // Debugging: Print field and value
            System.out.println("Mapping field: " + fieldName + ", Value: " + value);

            Object convertedValue = convertValue(field.getType(), value);
            field.set(entity, convertedValue);
        }

        return entity;
    }


}
