package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.path.FareCalculator;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource({"15,0,5", "15,500,9", "21,960,15", "10,1250,20", "58,0,66"})
    void findFare(int distance, int fare, int age) {
        List<Line> lines = List.of(
                new Line(1L, "1호선", "blue", 0),
                new Line(2L, "2호선", "green", 0)
        );
        FareCalculator fareCalculator = new FareCalculator();
        int actual = fareCalculator.findFare(distance, lines, age);
        assertThat(actual).isEqualTo(fare);
    }
}
