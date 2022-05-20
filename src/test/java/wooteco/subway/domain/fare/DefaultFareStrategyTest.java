package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultFareStrategyTest {

    @DisplayName("13 ~ 18의 요금을 계산한다")
    @Test
    void calculateTeenAgerFare() {
        DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        FareCondition fareCondition = new FareCondition(10, 13, 100);
        assertThat(defaultFareStrategy.calculateFare(fareCondition)).isEqualTo(800);
    }

    @DisplayName("6 ~ 12의 요금을 계산한다")
    @Test
    void calculateChildrenFare() {
        DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        FareCondition fareCondition = new FareCondition(10, 6, 100);
        assertThat(defaultFareStrategy.calculateFare(fareCondition)).isEqualTo(500);
    }

}
