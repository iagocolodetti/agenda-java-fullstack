package br.com.iagocolodetti.agenda.validation;

import br.com.iagocolodetti.agenda.dto.EmailDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

/**
 *
 * @author iagocolodetti
 */
public class ListEmailOneNotDeletedValidator implements ConstraintValidator<ListEmailOneNotDeleted, List<EmailDto>> {
    
    @Override
    public boolean isValid(List<EmailDto> list, ConstraintValidatorContext cxt) {
        return list.stream().anyMatch(e -> e.getId() == null || !e.isDeleted());
    }
}
