package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.domain.path.implement.MinimumDistanceStrategy;
import wooteco.subway.domain.path.PathStrategy;

class MinimumDistanceStrategyTest {

    private static final List<Station> stations = List.of(
            new Station(1L, "v1"),
            new Station(2L, "v2"),
            new Station(3L, "v3"),
            new Station(4L, "v4"),
            new Station(5L, "v5"),
            new Station(6L, "v6"),
            new Station(7L, "v7"));

    private static final Map<Long, Station> stationMap = Map.of(
            1L, stations.get(0),
            2L, stations.get(1),
            3L, stations.get(2),
            4L, stations.get(3),
            5L, stations.get(4),
            6L, stations.get(5),
            7L, stations.get(6));

    private static final List<Section> sections = List.of(
            new Section(1L, 1L, 1L, 2L, 5),
            new Section(2L, 1L, 2L, 3L, 5),
            new Section(3L, 1L, 3L, 4L, 5),
            new Section(4L, 2L, 2L, 5L, 2),
            new Section(5L, 2L, 5L, 4L, 2),
            new Section(6L, 3L, 6L, 7L, 2));

    private static final Map<Long, Section> sectionMap = Map.of(
            1L, sections.get(0),
            2L, sections.get(1),
            3L, sections.get(2),
            4L, sections.get(3),
            5L, sections.get(4),
            6L, sections.get(5));

    @DisplayName("최단 거리 계산하기")
    @ParameterizedTest(name = "{0}역 -> {1}역, 거리: {2}")
    @CsvSource(value = {"1, 2, 5", "1, 3, 10", "1, 4, 9", "2, 4, 4", "3, 5, 7"})
    void calculateMinimumDistance(Long fromId, Long toId, int distance) {
        // given
        PathStrategy strategy = new MinimumDistanceStrategy();
        Station from = stationMap.get(fromId);
        Station to = stationMap.get(toId);

        // when
        Path path = strategy.findPath(stations, sections, from, to);
        int result = path.getDistance();

        // then
        assertThat(result).isEqualTo(distance);
    }

    @DisplayName("최단 거리에 포함된 station을 구한다")
    @ParameterizedTest
    @MethodSource("provideStationsInPath")
    void getStationsInPath(Long fromId, Long toId, List<Station> expectedStations) {
        // given
        PathStrategy strategy = new MinimumDistanceStrategy();
        Station from = stationMap.get(fromId);
        Station to = stationMap.get(toId);

        // when
        Path path = strategy.findPath(stations, sections, from, to);
        List<Station> stationsInPath = path.getStationsInPath();

        // then
        assertThat(stationsInPath).isEqualTo(expectedStations);
    }

    private static Stream<Arguments> provideStationsInPath() {
        return Stream.of(
                Arguments.of(1L, 2L, List.of(stationMap.get(1L), stationMap.get(2L))),
                Arguments.of(1L, 3L, List.of(stationMap.get(1L), stationMap.get(2L), stationMap.get(3L))),
                Arguments.of(1L, 4L, List.of(stationMap.get(1L), stationMap.get(2L), stationMap.get(5L), stationMap.get(4L))),
                Arguments.of(2L, 4L, List.of(stationMap.get(2L), stationMap.get(5L), stationMap.get(4L)))
        );
    }

    @DisplayName("최단 거리에 포함된 section을 구한다")
    @ParameterizedTest
    @MethodSource("provideSectionsInPath")
    void getSectionsInPath(Long fromId, Long toId, List<Section> expectedSections) {
        // given
        PathStrategy strategy = new MinimumDistanceStrategy();
        Station from = stationMap.get(fromId);
        Station to = stationMap.get(toId);

        // when
        Path path = strategy.findPath(stations, sections, from, to);
        List<Section> sectionsInPath = path.getSectionsInPath();

        // then
        assertThat(sectionsInPath).isEqualTo(expectedSections);
    }

    private static Stream<Arguments> provideSectionsInPath() {
        return Stream.of(
                Arguments.of(1L, 2L, List.of(sectionMap.get(1L))),
                Arguments.of(1L, 3L, List.of(sectionMap.get(1L), sectionMap.get(2L))),
                Arguments.of(1L, 4L, List.of(sectionMap.get(1L), sectionMap.get(4L), sectionMap.get(5L))),
                Arguments.of(2L, 4L, List.of(sectionMap.get(4L), sectionMap.get(5L)))
        );
    }

    @DisplayName("갈 수 없는 두 역 사이에 대한 요청이 오면")
    @Test
    void getPath_Fail() {
        // given
        PathStrategy strategy = new MinimumDistanceStrategy();
        Station from = stationMap.get(1L);
        Station to = stationMap.get(7L);

        // when
        assertThatThrownBy(() -> strategy.findPath(stations, sections, from, to))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 두 역입니다.");
    }
}
