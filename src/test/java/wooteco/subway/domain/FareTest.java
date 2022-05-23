package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @DisplayName("추가 요금이 없는 경우의 운임 요금을 계산한다.")
    @CsvSource(value = {"9:1250", "10:1250", "11:1350", "33:1750", "50:2050", "58:2150"}, delimiter = ':')
    @ParameterizedTest
    void calculate(int distance, int expected) {
        Fare fare = new Fare(distance, 0);

        assertThat(fare.calculate()).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 있는 경우, 운임 요금에 추가하여 계산한다.")
    @CsvSource(value = {"9:1750", "10:1750", "11:1850", "33:2250", "50:2550", "58:2650"}, delimiter = ':')
    @ParameterizedTest
    void calculateWithExtraCharge(int distance, int expected) {
        Fare fare = new Fare(distance, 500);

        assertThat(fare.calculate()).isEqualTo(expected);
    }
}
