package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @DisplayName("거리를 받아, 운임 비용을 반환한다.")
    @ParameterizedTest
    @CsvSource({"10, 1250", "11, 1350", "16, 1450", "51, 2150"})
    void calculateFare(int distance, int expected) {
        FareCalculator fareCalculator = FareCalculator.getInstance();
        int fare = fareCalculator.calculate(distance);

        assertThat(fare).isEqualTo(expected);
    }
}
