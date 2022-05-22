package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ExtraFareTest {

    @ParameterizedTest(name = "{0}km일 때 요금은 {1}원이다")
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150", "15.1,1450"})
    void calculate(double distance, int expected) {
        int fare = ExtraFare.calculateTotalFare(distance);

        assertThat(fare).isEqualTo(expected);
    }
}