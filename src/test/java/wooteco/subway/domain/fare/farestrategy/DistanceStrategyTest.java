package wooteco.subway.domain.fare.farestrategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.farestrategy.charge.DistanceStandard;

class DistanceStrategyTest {

    @ParameterizedTest
    @DisplayName("거리 기반 요금 계산")
    @CsvSource({"10,1250", "25,1550", "50,2050", "90,2550"})
    void calculate(int distance, long expected) {
        ChargeStrategy distanceStrategy = DistanceStandard.findStrategy(distance);
        long calculated = distanceStrategy.calculate(1250);
        assertThat(calculated).isEqualTo(expected);
    }
}
