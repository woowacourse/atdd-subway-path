package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

    @ParameterizedTest(name = "{0}km일 때 요금은 {1}원이다")
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150"})
    void calculate(int distance, int expected) {
        FareCalculator fareCalculator = new FareCalculator();

        int fare = fareCalculator.execute(distance);

        assertThat(fare).isEqualTo(expected);
    }
}
