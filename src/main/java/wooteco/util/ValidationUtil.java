package wooteco.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import wooteco.common.exception.RequiredParameterValidationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil {
    public static void validateRequestedParameter(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw RequiredParameterValidationException.from(bindingResult);
        }
    }
}
