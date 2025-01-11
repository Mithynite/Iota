package iota.com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name(); // Name of the column in the database
    boolean isPrimaryKey() default false; // Indicates if the column is a primary key
    boolean canBeNull() default true;
}
