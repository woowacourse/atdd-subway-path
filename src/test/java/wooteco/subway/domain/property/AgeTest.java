package wooteco.subway.domain.property;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AgeTest {
    @Test
    @DisplayName("나이를 입력하지 않은 경우 예외 발생")
    void throwsExceptionWithEmptyAge() {
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(()->new Age(null))
                .withMessageContaining("나이는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("1 이상의 나이를 입력하지 않은 경우 예외 발생")
    void throwsExceptionWithNotPositive() {
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(()->new Age(0))
                .withMessageContaining("나이는 0 이하가 될 수 없습니다.");
    }
}
