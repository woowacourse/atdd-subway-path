package wooteco.subway.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리로 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"14,1350", "40,1850", "58, 2150"})
    void calculateFareByDistance(int distance, int resultFare) {
        int fare = FareCalculator.calculate(distance);
        assertThat(fare).isEqualTo(resultFare);
    }
}
