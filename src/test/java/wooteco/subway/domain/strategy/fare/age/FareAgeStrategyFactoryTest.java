package wooteco.subway.domain.strategy.fare.age;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.FareAgeStrategyNotFoundException;

class FareAgeStrategyFactoryTest {

    @Test
    @DisplayName("나이별 요금 전략에 해당하는 나이가 없을 경우 예외가 발생한다.")
    void invalidNotFindFareAgeStrategy() {
        assertThatThrownBy(() -> FareAgeStrategyFactory.getStrategy(-1))
                .isInstanceOf(FareAgeStrategyNotFoundException.class);
    }
}
