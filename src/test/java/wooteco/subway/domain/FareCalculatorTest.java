package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150"})
    @DisplayName("성인인 경우의 금액을 계산한다.")
    void calculateFareTest(int distance, int fare) {
        assertThat(FareCalculator.calculate(distance, 0, 25)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource({"9,720", "12,800", "16,880", "58,1440"})
    @DisplayName("청소년인 경우의 금액을 계산한다.")
    void calculateFareTestWithTeenager(int distance, int fare) {
        assertThat(FareCalculator.calculate(distance, 0, 15)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource({"9,450", "12,500", "16,550", "58,900"})
    @DisplayName("어린이인 경우의 금액을 계산한다.")
    void calculateFareTestWithChild(int distance, int fare) {
        assertThat(FareCalculator.calculate(distance, 0, 8)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource({"9,2050", "12,2150", "16,2250", "58,2950"})
    @DisplayName("추가 요금이 있는 경우의 금액을 계산한다.")
    void calculateFareTestWithExtraFare(int distance, int fare) {
        assertThat(FareCalculator.calculate(distance, 800, 25)).isEqualTo(fare);
    }

}
