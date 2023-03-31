package br.com.iagocolodetti.agenda.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author iagocolodetti
 */
@Target({ FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ListEmailOneNotDeletedValidator.class)
public @interface ListEmailOneNotDeleted {
    
    public String message() default "The list must have one email not deleted";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
