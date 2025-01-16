package iota.com.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom @Transient annotation to mark fields not to be persisted.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) // Ensure it can be applied to fields
public @interface Transient {
}
