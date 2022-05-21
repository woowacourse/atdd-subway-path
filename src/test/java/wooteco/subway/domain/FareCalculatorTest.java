package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource({"10,1250","15,1350","21,1550","58,2150", "59,2250"})
    void findFare (int distance, int fare) {
        List<Station> stations = List.of(
            new Station(1L, "강남역"),
            new Station(2L, "강남역")
        );
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, distance))
        );
        Path path = new Path(stations, sections, distance);

        int actual = new FareCalculator().calculateFare(path);

        assertThat(actual).isEqualTo(fare);
    }
}
