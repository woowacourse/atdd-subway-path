package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicFareStrategyTest {

    @Test
    @DisplayName("기본 거리에 대한 운임을 계산한다.")
    void calculateBasicDistance() {
        FareStrategy fareStrategy = new BasicFareStrategy();

        int fare = fareStrategy.calculateFare(9, 0, 20);

        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 50이하일 경우 5km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50UnderDistance() {
        FareStrategy fareStrategy = new BasicFareStrategy();

        int fare = fareStrategy.calculateFare(12, 0, 20);

        assertThat(fare).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리가 50초과일 경우 8km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50OverDistance() {
        FareStrategy fareStrategy = new BasicFareStrategy();

        int fare = fareStrategy.calculateFare(58, 0, 20);

        assertThat(fare).isEqualTo(2150);
    }

    @Test
    @DisplayName("추가 요금이 붙으면 합산해서 계산한다.")
    void addExtraFare() {
        FareStrategy fareStrategy = new BasicFareStrategy();

        int fare = fareStrategy.calculateFare(9, 900, 20);

        assertThat(fare).isEqualTo(2150);
    }

}
