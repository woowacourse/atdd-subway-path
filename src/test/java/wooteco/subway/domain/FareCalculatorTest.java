package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150"})
    @DisplayName("금액을 계산한다.")
    void calculateFareTest(int distance, int fare) {
        assertThat(FareCalculator.calculate(distance, Collections.EMPTY_LIST, 25)).isEqualTo(fare);
    }


}
