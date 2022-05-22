package wooteco.subway.domain.strategy.fare.distance;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.FareDistanceStrategyNotFoundException;

class FareDistanceStrategyFactoryTest {

    @Test
    @DisplayName("구간별 요금 전략에 해당하는 구간이 없을 경우 예외가 발생한다.")
    void invalidNotFindFareDistanceStrategy() {
        assertThatThrownBy(() -> FareDistanceStrategyFactory.getStrategy(-1))
                .isInstanceOf(FareDistanceStrategyNotFoundException.class);
    }
}
