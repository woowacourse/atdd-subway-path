package wooteco.subway.station.infrastructure;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;
import java.util.regex.Pattern;

@Component
public class StationNameValidator implements ConstraintValidator<StationName, String> {

    private static final Pattern PATTERN = Pattern.compile("[가-힣|0-9]*역$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!PATTERN.matcher(value).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("역이름은 한글, 숫자여야하고 마지막 글자에 역이 포함되어 있어야합니다. 현재 입력 값: {0}", value))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
