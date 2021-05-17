package wooteco.subway.station.infrastructure;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = StationNameValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface StationName {

    String message() default "Station Name is not allow ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}