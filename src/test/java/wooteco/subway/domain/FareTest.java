package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @DisplayName("거리, 가장 비싼 추가 운임, 나이를 받아 운임을 계산한다.")
    @ParameterizedTest
    @CsvSource({"10, 300, 21, 1550", "10, 300, 13, 960", "10, 300, 12, 600",
            "11, 300, 21, 1650", "11, 300, 13, 1040", "11, 300, 12, 650",
            "16, 300, 21, 1750", "16, 300, 13, 1120", "16, 300, 12, 700",
            "51, 300, 21, 2450", "51, 300, 13, 1680", "51, 300, 12, 1050"})
    void calculate(int distance, int highestExtraFare, int age, int expected) {
        Fare fare = new Fare(distance, highestExtraFare, age);

        int actual = fare.value();

        assertThat(actual).isEqualTo(expected);
    }
}
