package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareStrategyFactoryTest {

    @DisplayName("1~10 km 시 기본 요금 계산전략을 가져온다.")
    @Test
    void getDefaultFareStrategy() {
        assertThat(FareStrategyFactory.get(9)).isInstanceOf(DefaultFareStrategy.class);
    }

    @DisplayName("11 ~ 50 km 시 FirstOver 계산전략을 가져온다.")
    @Test
    void getFirstOverFareStrategy() {
        assertThat(FareStrategyFactory.get(50)).isInstanceOf(FirstOverFareStrategy.class);
    }

    @DisplayName("50 km 초과시 Max 계산전략을 가져온다.")
    @Test
    void getMaxFareStrategy() {
        assertThat(FareStrategyFactory.get(51)).isInstanceOf(MaxFareStrategy.class);
    }
}
