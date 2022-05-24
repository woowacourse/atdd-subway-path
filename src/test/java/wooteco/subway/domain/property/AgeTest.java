package wooteco.subway.domain.property;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AgeTest {
    @Test
    @DisplayName("나이를 입력하지 않은 경우 예외 발생")
    void throwsExceptionWithEmptyAge() {
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> Age.from(null))
                .withMessageContaining("나이는 필수 입력값입니다.");
    }

    @Test
    @DisplayName("0 이상의 나이를 입력하지 않은 경우 예외 발생")
    void throwsExceptionWithNotPositive() {
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> Age.from(-1))
                .withMessageContaining("나이는 0 이하가 될 수 없습니다.");
    }

    @Test
    @DisplayName("영유아라면 전액할인")
    void baby() {
        assertThat(Age.from(3).calculateDiscountFare(1000)).isEqualTo(1000);
    }

    @Test
    @DisplayName("어린이라면 350원을 공제한 금액의 50% 할인")
    void child() {
        assertThat(Age.from(10).calculateDiscountFare(1000)).isEqualTo(325);
    }

    @Test
    @DisplayName("청소년이라면 350원을 공제한 금액의 20% 할인")
    void teenager() {
        assertThat(Age.from(15).calculateDiscountFare(1000)).isEqualTo(130);
    }

    @Test
    @DisplayName("성인이라면 할인없음")
    void adult() {
        assertThat(Age.from(20).calculateDiscountFare(1000)).isEqualTo(0);
    }

}
