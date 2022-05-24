package wooteco.subway.domain.strategy.fare.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceFareManagerTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    @DisplayName("기본운임을 계산할 수 있다.")
    void calculateBasicDistance(int distance) {
        // given
        DistanceFareManager distanceFareManager = DistanceFareManagerFactory.createDistanceFareManager();

        // when
        int fare = distanceFareManager.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 11일 경우 5km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate_distance11() {
        // given
        DistanceFareManager distanceFareManager = DistanceFareManagerFactory.createDistanceFareManager();

        // when
        int fare = distanceFareManager.calculateFare(11);

        // then
        assertThat(fare).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리가 50일 경우 5km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate_distance50() {
        // given
        DistanceFareManager distanceFareManager = DistanceFareManagerFactory.createDistanceFareManager();

        // when
        int fare = distanceFareManager.calculateFare(50);

        // then
        assertThat(fare).isEqualTo(2050);
    }

    @ParameterizedTest
    @ValueSource(ints = {51})
    @DisplayName("거리가 50초과 경우 8km 마다 100원이 추가되어 계산할 수 있다.")
    void calculate50OverDistance(int distance) {
        // given
        DistanceFareManager distanceFareManager = DistanceFareManagerFactory.createDistanceFareManager();

        // when
        int fare = distanceFareManager.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(2150);
    }
}
