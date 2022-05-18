package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicFareStrategyTest {

    @Test
    @DisplayName("기본 거리에 대한 운임을 계산한다.")
    void calculateBasicDistance() {
        FareStrategy fareStrategy = new BasicFareStrategy();

        int fare = fareStrategy.calculateFare(9);

        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 50미만일 경우 5km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50UnderDistance() {
        FareStrategy farePolicy = new BasicFareStrategy();

        int fare = farePolicy.calculateFare(12);

        assertThat(fare).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리가 50이상일 경우 8km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50OverDistance() {
        FareStrategy farePolicy = new BasicFareStrategy();

        int fare = farePolicy.calculateFare(58);

        assertThat(fare).isEqualTo(2150);
    }

}
