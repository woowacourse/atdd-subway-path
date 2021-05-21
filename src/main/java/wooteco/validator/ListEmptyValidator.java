package wooteco.validator;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import wooteco.annotation.ListNotEmpty;

public class ListEmptyValidator implements ConstraintValidator<ListNotEmpty, List<Object>> {

    @Override
    public boolean isValid(List<Object> value, ConstraintValidatorContext context) {
        return !value.isEmpty();
    }
}
