package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SpecialExtraFareStrategyTest {

    private ExtraFareStrategy strategy = new SpecialExtraFareStrategy();

    @DisplayName("50Km 이상은 8Km 당 100원 씩 초과 요금을 부과한다.")
    @ParameterizedTest
    @CsvSource({"57,900", "59,1000"})
    void calculate50KmOver(int distance, int expectExtraFare) {
        int extraFare = strategy.calculate(distance);

        assertThat(extraFare).isEqualTo(expectExtraFare);
    }
}
