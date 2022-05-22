package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.path.FareCalculator;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource({"10,1250","15,1350","21,1550","58,2150"})
    void findFare (int distance, int fare) {
        FareCalculator fareCalculator = new FareCalculator();
        int actual = fareCalculator.findFare(distance ,
                List.of(new Line(1L, "1호선", "blue", 0), new Line(2L, "2호선", "green", 0)));
        assertThat(actual).isEqualTo(fare);
    }
}
