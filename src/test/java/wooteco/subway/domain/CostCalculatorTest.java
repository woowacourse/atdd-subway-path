package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CostCalculatorTest {

    @DisplayName("거리 계산하기 - 추가 요금 없음")
    @ParameterizedTest(name = "{0} km -> 요금 {1}원 예상")
    @CsvSource(value = {"5,0,1250", "44,0,1950", "60,0,2250"})
    void calculateCost(int distance, int extraFare, int expected) {
        // given

        // when
        int result = CostCalculator.calculate(distance, extraFare);

        // then
        assertThat(result).isEqualTo(expected);
    }
}