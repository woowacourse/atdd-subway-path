package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.fare.strategy.DefaultFareStrategy;

class FareCalculatorTest {

    @DisplayName("거리로 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10, 1250", "11, 1350", "50, 2050", "51, 2150"})
    void calculateFareByDistance(int distance, int resultFare) {
        FareCalculator fareCalculator = new FareCalculator(new DefaultFareStrategy());
        int fare = fareCalculator.calculate(distance);
        assertThat(fare).isEqualTo(resultFare);
    }
}
