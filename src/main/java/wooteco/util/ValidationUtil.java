package wooteco.util;

import org.springframework.validation.BindingResult;
import wooteco.exception.RequiredParameterValidationException;

public class ValidationUtil {
    public static void validateRequestedParameter(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw RequiredParameterValidationException.from(bindingResult);
        }
    }
}
