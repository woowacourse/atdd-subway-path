package wooteco.subway.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @DisplayName("나이가 음수일 경우 예외를 발생한다.")
    @Test
    void blank_validation_test() {
        // given
        PathRequest userCreateRequestDto = new PathRequest(1L, 3L,-1);

        // when
        Set<ConstraintViolation<PathRequest>> violations = validator.validate(userCreateRequestDto);

        // then
        assertThat(violations).isNotEmpty();
        violations.forEach(error -> {
                    assertThat(error.getMessage()).isEqualTo("나이는 0 이상의 수만 가능합니다.");
                });
    }
}
