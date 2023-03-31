package br.com.iagocolodetti.agenda.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

/**
 *
 * @author iagocolodetti
 */
public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List<?>> {
    
    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext cxt) {
        return list != null && !list.isEmpty();
    }
}
