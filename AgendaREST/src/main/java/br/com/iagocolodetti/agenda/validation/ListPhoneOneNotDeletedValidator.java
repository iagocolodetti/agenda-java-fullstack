package br.com.iagocolodetti.agenda.validation;

import br.com.iagocolodetti.agenda.dto.PhoneDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

/**
 *
 * @author iagocolodetti
 */
public class ListPhoneOneNotDeletedValidator implements ConstraintValidator<ListPhoneOneNotDeleted, List<PhoneDto>> {
    
    @Override
    public boolean isValid(List<PhoneDto> list, ConstraintValidatorContext cxt) {
        return list.stream().anyMatch(p -> p.getId() == null || !p.isDeleted());
    }
}
