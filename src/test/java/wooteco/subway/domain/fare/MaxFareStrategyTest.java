package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MaxFareStrategyTest {

    @DisplayName("요금을 계산한다")
    @Test
    void calculateFare() {
        MaxFareStrategy maxFareStrategy = new MaxFareStrategy();
        FareCondition fareCondition = new FareCondition(51, 15, 100);
        assertThat(maxFareStrategy.calculateFare(fareCondition)).isEqualTo(2250);
    }

}
