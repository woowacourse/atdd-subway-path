package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource({"10,1250","15,1350","21,1550","58,2150"})
    void findFare (int distance, int fare) {

        FareCalculator fareCalculator = new FareCalculator();
        int actual = fareCalculator.findFare(distance);
        assertThat(actual).isEqualTo(fare);
    }
}
