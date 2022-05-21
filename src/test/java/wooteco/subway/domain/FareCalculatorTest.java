package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {

    @ParameterizedTest(name = "거리 : {0} 기대요금 : {1}")
    @CsvSource(value = {"10,1250", "11,1350", "15,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250"})
    @DisplayName("거리별 요금 테스트")
    void calculateFare(int distance, long expected) {
        // given
        final FareCalculator fareCalculator = new FareCalculator();

        // when
        final Long actual = fareCalculator.calculate(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
