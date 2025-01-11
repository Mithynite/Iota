package iota.com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Indicates that this annotation should be available at runtime
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
    String foreignKeyName(); // Name of the foreign key column in the database
}
