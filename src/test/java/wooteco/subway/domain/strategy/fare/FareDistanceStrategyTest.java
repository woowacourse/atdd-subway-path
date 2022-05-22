package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.strategy.fare.distance.DefaultStrategy;
import wooteco.subway.domain.strategy.fare.distance.Over10KmStrategy;
import wooteco.subway.domain.strategy.fare.distance.Over50KmStrategy;

class FareDistanceStrategyTest {

    @Test
    @DisplayName("기본운임을 계산할 수 있다.")
    void calculateBasicDistance() {
        // given
        DefaultStrategy fareStrategy = new DefaultStrategy();

        // when
        int fare = fareStrategy.distanceFare(9);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 50미만일 경우 5km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50UnderDistance() {
        // given
        Over10KmStrategy fareStrategy = new Over10KmStrategy();

        // when
        int fare = fareStrategy.distanceFare(12);

        // then
        assertThat(fare).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리가 50이상일 경우 8km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50OverDistance() {
        // given
        Over50KmStrategy fareStrategy = new Over50KmStrategy();

        // when
        int fare = fareStrategy.distanceFare(58);

        // then
        assertThat(fare).isEqualTo(2150);
    }
}
