package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.strategy.fare.distance.DefaultStrategy;

class FarePolicyTest {

    @Test
    @DisplayName("거리의 정보와 가장 높은 추가운임 비용을 통해 운임비용을 계산할 수 있다.")
    void calculateFare() {
        // given
        FarePolicy farePolicy = new FarePolicy(new DefaultStrategy());

        // when
        int fare = farePolicy.getFare(10, 1000);

        // then
        assertThat(fare).isEqualTo(2250);
    }
}
