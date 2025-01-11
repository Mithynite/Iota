package iota.com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Indicates that this annotation should be available at runtime
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToMany {
    String joinTable(); // Name of the intermediate join table
    String joinColumns(); // Columns joining the current entity
    String inverseJoinColumns(); // Columns joining the related entity
}
