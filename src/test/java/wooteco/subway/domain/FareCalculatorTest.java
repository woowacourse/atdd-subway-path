package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {

    @DisplayName("거리별 요금 계산 (추가 요금 X)")
    @ParameterizedTest(name = "{0} km -> 요금 {2}원 예상")
    @CsvSource(value = {"1,0,1250", "10,0,1250", "11,0,1350", "30,0,1650", "50,0,2050", "51,0,2150", "60,0,2250"})
    void calculateCostByDistance(int distance, int extraFare, int expected) {
        // given
        FareCalculator fareCalculator = new FareCalculator(distance, extraFare);

        // when
        int result = fareCalculator.calculate();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("추가 요금별 계산 (거리 10km 고정)")
    @ParameterizedTest(name = "{1} 원 -> 요금 {2}원 예상")
    @CsvSource(value = {"10,0,1250", "10,100,1350", "10,9000,10250"})
    void calculateCostByExtraForce(int distance, int extraFare, int expected) {
        // given
        FareCalculator fareCalculator = new FareCalculator(distance, extraFare);

        // when
        int result = fareCalculator.calculate();

        // then
        assertThat(result).isEqualTo(expected);
    }
}