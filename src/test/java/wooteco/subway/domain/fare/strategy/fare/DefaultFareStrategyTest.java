package wooteco.subway.domain.fare.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.utils.TestConstants.PARAMETERIZED_NAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DefaultFareStrategyTest {

    @DisplayName("기본적인 요금 계산 정책을 적용하여 계산한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource({"10, 100, 1350", "11, 100, 1450", "50, 500, 2550", "51, 200, 2350"})
    void calculate(double distance, int extraFare, int expect) {
        DefaultFareStrategy defaultFareStrategy = new DefaultFareStrategy();
        int result = defaultFareStrategy.calculate(distance, extraFare);

        assertThat(result).isEqualTo(expect);
    }
}
