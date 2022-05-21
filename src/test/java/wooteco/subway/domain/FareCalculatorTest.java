package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

    @DisplayName("거리에 따른 요금 계산")
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
        Map<Long, Integer> extraFares = Map.of(1L, 0);

        int actual = new FareCalculator(extraFares).calculateFare(path);

        assertThat(actual).isEqualTo(fare);
    }

    @DisplayName("추가 요금이 있는 노선의 요금 계산")
    @Test
    void findHasExtraFare () {
        List<Station> stations = List.of(
            new Station(1L, "강남역"),
            new Station(2L, "강남역"),
            new Station(3L, "역삼역")
        );
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, 10)),
            new Section(1L, 2L, new SectionEdge(2L, 3L, 3))
        );
        Path path = new Path(stations, sections, 13);
        Map<Long, Integer> extraFares = Map.of(1L, 500, 2L, 800);

        int actual = new FareCalculator(extraFares).calculateFare(path);

        assertThat(actual).isEqualTo(2150);
    }
}
