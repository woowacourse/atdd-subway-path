package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FareTest {

    @DisplayName("거리가 0km 보다 작은 경우 오류가 발생한다.")
    @Test
    void validateDistance() {
        assertThatThrownBy(() -> new Fare(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 음수가 될 수 없습니다.");
    }

    @DisplayName("거리에 따른 어른 운임을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10,1250", "12,1350", "50,2050", "58,2150"})
    void calculate(int input, int expected) {
        Fare fare = new Fare(input);

        assertThat(fare.calculate(20, 0)).isEqualTo(expected);
    }

    @DisplayName("추가요금이 있는 운임을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9,2150", "10,2150", "12,2250", "50,2950", "58,3050"})
    void calculateWithExtraFare(int input, int expected) {
        Fare fare = new Fare(input);

        assertThat(fare.calculate(20, 900)).isEqualTo(expected);
    }

    @DisplayName("청소년 운임을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9,720", "10,720", "12,800", "50,1360", "58,1440"})
    void calculateTeenagerFare(int input, int expected) {
        Fare fare = new Fare(input);

        assertThat(fare.calculate(18, 0)).isEqualTo(expected);
    }

    @DisplayName("어린이 운임을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9,450", "10,450", "12,500", "50,850", "58,900"})
    void calculateChildrenFare(int input, int expected) {
        Fare fare = new Fare(input);

        assertThat(fare.calculate(12, 0)).isEqualTo(expected);
    }

    @DisplayName("무료 운임을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {5, 65})
    void calculateFreeFare(int age) {
        Fare fare = new Fare(10);

        assertThat(fare.calculate(age, 0)).isEqualTo(0);
    }
}
