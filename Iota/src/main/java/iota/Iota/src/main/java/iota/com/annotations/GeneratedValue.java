package iota.com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Indicates that this annotation should be available at runtime
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {
    GenerationType strategy() default GenerationType.AUTO; // Default to AUTO increment
}

