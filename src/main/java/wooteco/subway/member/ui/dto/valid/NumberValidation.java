package wooteco.subway.member.ui.dto.valid;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Positive
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface NumberValidation {

    String message() default "입력이 부정확 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
