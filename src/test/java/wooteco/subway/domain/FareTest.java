package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("거리가 0km 보다 작은 경우 오류가 발생한다.")
    @Test
    void validateDistance() {
        assertThatThrownBy(() -> new Fare(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 음수가 될 수 없습니다.");
    }

    @DisplayName("거리에 따른 운임을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10,1250", "12,1350", "50,2050", "58,2150"})
    void calculate(int input, int expected) {
        Fare fare = new Fare(input);

        assertThat(fare.calculate()).isEqualTo(expected);
    }
}
