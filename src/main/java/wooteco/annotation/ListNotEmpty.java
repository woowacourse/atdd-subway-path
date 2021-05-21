package wooteco.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import wooteco.validator.ListEmptyValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ListEmptyValidator.class)
public @interface ListNotEmpty {
    String message() default "리스트 공백 체크";
    Class[] groups() default {};
    Class[] payload() default {};
}
