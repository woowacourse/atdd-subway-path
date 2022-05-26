package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.PositiveDigitException;

class FareByAgeTest {

    @DisplayName("age 가 0초과, 5이하, 65이상일 때, 요금은 무료여야한다.")
    @ParameterizedTest
    @ValueSource(ints = {5, 65})
    void calculateFreeAgeFare(int age) {
        int fare = FareByAge.findFare(age, 1250);

        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("age 가 6이상 12이하일 때, 청구된 요금에서 350원을 뺀 값에서 50% 할인된 값이여야한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void calculateChildrenAgeFare(int age) {
        int fare = FareByAge.findFare(age, 1250);

        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("age 가 13이상 18이하일 때, 청구된 요금에서 350원을 뺀 값에서 20% 할인된 값이여야한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void calculateTeenagerAgeFare(int age) {
        int fare = FareByAge.findFare(age, 1250);

        assertThat(fare).isEqualTo(720);
    }

    @DisplayName("나이가 양수가 아닐 경우 예외를 발생시킨다.")
    @Test
    void calculateAgeFareExceptionNotPositiveAge() {
        assertThatThrownBy(() -> FareByAge.findFare(0, 1250))
                .isInstanceOf(PositiveDigitException.class)
                .hasMessage("나이는 양수여야 합니다.");
    }
}
