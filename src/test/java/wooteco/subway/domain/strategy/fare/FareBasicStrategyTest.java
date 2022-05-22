package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.strategy.fare.basic.BasicDistanceFareStrategy;
import wooteco.subway.domain.strategy.fare.basic.DistanceFareStrategy;
import wooteco.subway.domain.strategy.fare.basic.DistanceFareStrategyFactory;

class FareBasicStrategyTest {

    @Test
    @DisplayName("기본운임을 계산할 수 있다.")
    void calculateBasicDistance() {
        // given
        DistanceFareStrategy farePolicy = DistanceFareStrategyFactory.getDistanceFareStrategy(9);

        // when
        int fare = farePolicy.calculateFare(9);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 50미만일 경우 5km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50UnderDistance() {
        // given
        DistanceFareStrategy farePolicy = DistanceFareStrategyFactory.getDistanceFareStrategy(12);

        // when
        int fare = farePolicy.calculateFare(12);

        // then
        assertThat(fare).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리가 50이상일 경우 8km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50OverDistance() {
        // given
        DistanceFareStrategy farePolicy = DistanceFareStrategyFactory.getDistanceFareStrategy(58);

        // when
        int fare = farePolicy.calculateFare(58);

        // then
        assertThat(fare).isEqualTo(2150);
    }
}
