package wooteco.subway.domain.strategy.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BasicExtraFareStrategyTest {

    private ExtraFareStrategy strategy = new BasicExtraFareStrategy();

    @DisplayName("10Km 이상 50Km 이하는 5Km 당 100원 씩 초과 요금을 부과한다.")
    @ParameterizedTest
    @CsvSource({"11,100", "49,800", "20,200"})
    void calculate50KmOver(int distance, int expectExtraFare) {
        int extraFare = strategy.calculate(distance);

        assertThat(extraFare).isEqualTo(expectExtraFare);
    }
}
