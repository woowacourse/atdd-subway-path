package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultFareStrategyTest {

    @DisplayName("요금을 계산한다")
    @Test
    void calculateFare() {
        DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        FareCondition fareCondition = new FareCondition(10, 15, 100);
        assertThat(defaultFareStrategy.calculateFare(fareCondition)).isEqualTo(1350);
    }

}
