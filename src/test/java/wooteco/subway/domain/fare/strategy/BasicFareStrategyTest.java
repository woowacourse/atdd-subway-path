package wooteco.subway.domain.fare.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("추가 요금에 대한 기본 전략을 테스트한다.")
class BasicFareStrategyTest {

    private ExtraFareStrategy strategy = new BasicExtraFareStrategy();

    @DisplayName("10Km 이하는 초과요금이 없다.")
    @Test
    void calculate10KmUnder() {
        int extraFare = strategy.calculate(10);

        assertThat(extraFare).isEqualTo(0);
    }

    @DisplayName("10Km 이상 50Km 이하는 5Km 당 100원 씩 초과 요금을 부과한다.")
    @ParameterizedTest
    @CsvSource({"11,100", "16,200", "21,300", "49,800"})
    void calculate10KmOver50KmUnder(int distance, int expectExtraFare) {
        int extraFare = strategy.calculate(distance);

        assertThat(extraFare).isEqualTo(expectExtraFare);
    }
}
