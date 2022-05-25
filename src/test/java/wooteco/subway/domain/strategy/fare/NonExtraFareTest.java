package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NonExtraFareStrategyTest {

    private ExtraFareStrategy strategy = new NonExtraFareStrategy();

    @DisplayName("10Km 이하는 초과요금이 없다.")
    @Test
    void calculate10KmUnder() {
        int extraFare = strategy.calculate(10);

        assertThat(extraFare).isEqualTo(0);
    }

}
