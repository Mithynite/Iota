package iota.com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Indicates that this annotation should be available at runtime
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
    String mappedBy(); // Field name in the other entity class
}
