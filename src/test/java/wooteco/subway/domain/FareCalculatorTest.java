package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FareCalculatorTest {

    private List<Station> stations;

    @BeforeEach
    void setUp() {
        stations = List.of(
            new Station(1L, "강남역"),
            new Station(2L, "강남역"),
            new Station(3L, "역삼역")
        );
    }

    @DisplayName("기본 거리 요금 계산")
    @ParameterizedTest
    @ValueSource(ints = {5, 6, 10})
    void calculateFare(int distance) {
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, distance))
        );
        Path path = new Path(stations, sections, distance);
        Map<Long, Integer> extraFares = Map.of(1L, 0);

        int actual = new FareCalculator(extraFares, Passenger.valueOf(19)).calculateFare(path);

        assertThat(actual).isEqualTo(1250);
    }

    @DisplayName("1차 기준 거리 초과 시 요금 계산")
    @ParameterizedTest
    @CsvSource({"11,1350", "15,1350", "21,1550", "26,1650"})
    void calculateOverPrimaryDistance(int distance, int fare) {
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, distance))
        );
        Path path = new Path(stations, sections, distance);
        Map<Long, Integer> extraFares = Map.of(1L, 0);

        int actual = new FareCalculator(extraFares, Passenger.valueOf(19)).calculateFare(path);

        assertThat(actual).isEqualTo(fare);
    }

    @DisplayName("2차 기준 거리 초과 시 요금 계산")
    @ParameterizedTest
    @CsvSource({"51, 2150", "58,2150", "59,2250", "67,2350"})
    void calculateOverSecondaryDistance(int distance, int fare) {
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, distance))
        );
        Path path = new Path(stations, sections, distance);
        Map<Long, Integer> extraFares = Map.of(1L, 0);

        int actual = new FareCalculator(extraFares, Passenger.valueOf(19)).calculateFare(path);

        assertThat(actual).isEqualTo(fare);
    }

    @DisplayName("추가 요금이 있는 노선의 요금 계산")
    @Test
    void calculateHasExtraFare() {
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, 10)),
            new Section(1L, 2L, new SectionEdge(2L, 3L, 3))
        );
        Path path = new Path(stations, sections, 13);
        Map<Long, Integer> extraFares = Map.of(1L, 500, 2L, 800);

        int actual = new FareCalculator(extraFares, Passenger.valueOf(21)).calculateFare(path);

        assertThat(actual).isEqualTo(2150);
    }

    @DisplayName("청소년 지하철 요금 계산")
    @ParameterizedTest
    @ValueSource(ints = {13, 16, 18})
    void calculateYouthFare(int age) {
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, 10)),
            new Section(1L, 2L, new SectionEdge(2L, 3L, 3))
        );
        Path path = new Path(stations, sections, 13);
        Map<Long, Integer> extraFares = Map.of(1L, 500, 2L, 800);

        int actual = new FareCalculator(extraFares, Passenger.valueOf(age)).calculateFare(path);

        assertThat(actual).isEqualTo(1440);
    }

    @DisplayName("어린이 지하철 요금 계산")
    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    void calculateChildFare(int age) {
        List<Section> sections = List.of(
            new Section(1L, 1L, new SectionEdge(1L, 2L, 10)),
            new Section(1L, 2L, new SectionEdge(2L, 3L, 3))
        );
        Path path = new Path(stations, sections, 13);
        Map<Long, Integer> extraFares = Map.of(1L, 500, 2L, 800);

        int actual = new FareCalculator(extraFares, Passenger.valueOf(age)).calculateFare(path);

        assertThat(actual).isEqualTo(900);
    }
}
