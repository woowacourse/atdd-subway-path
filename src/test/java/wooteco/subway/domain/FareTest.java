package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.Fare;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
    @ParameterizedTest
    @CsvSource(value = {"9:0:20:1250", "10:0:20:1250", "11:0:20:1350", "49:0:20:2050", "50:0:20:2050", "51:0:20:2150"}, delimiter = ':')
    @DisplayName("거리에 따라 요금을 계산한다.")
    void calculateDistanceFare(int distance, int lineFare, int age, double expected) {
        Fare fare = new Fare(distance, lineFare, age);
        double actual = fare.getAmount();
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"9:0:2:0", "9:0:5:0", "9:0:6:450", "9:0:12:450", "9:0:13:720", "9:0:18:720", "9:0:19:1250"}, delimiter = ':')
    @DisplayName("나이에 따라 요금을 계산한다.")
    void calculateAgeFare(int distance, int lineFare, int age, double expected) {
        Fare fare = new Fare(distance, lineFare, age);
        double actual = fare.getAmount();
        assertThat(actual).isEqualTo(expected);
    }
}
