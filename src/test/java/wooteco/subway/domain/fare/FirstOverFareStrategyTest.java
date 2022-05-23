package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FirstOverFareStrategyTest {

    @DisplayName("요금을 계산한다")
    @Test
    void calculateFare() {
        FirstOverFareStrategy firstOverFareStrategy = new FirstOverFareStrategy();
        FareCondition fareCondition = new FareCondition(11, 15, 100);
        assertThat(firstOverFareStrategy.calculateFare(fareCondition)).isEqualTo(880);
    }
}
