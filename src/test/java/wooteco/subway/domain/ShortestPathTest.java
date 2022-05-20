package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ShortestPathTest {

    private static final Station station1 = new Station(1L, "v1");
    private static final Station station2 = new Station(2L, "v2");
    private static final Station station3 = new Station(3L, "v3");
    private static final Station station4 = new Station(4L, "v4");

    private static final List<Station> stations = List.of(station1, station2, station3, station4);

    private static final Long lineId1 = 1L;
    private static final Long lineId2 = 2L;

    private static final List<Section> sections = List.of(
            new Section(1L, lineId2, station1.getId(), station4.getId(), 1),
            new Section(2L, lineId1, station1.getId(), station4.getId(), 2),
            new Section(3L, lineId1, station4.getId(), station2.getId(), 2),
            new Section(4L, lineId1, station2.getId(), station3.getId(), 2)
    );

    @DisplayName("최단 경로 찾아주는 객체를 초기화한다.")
    @Test
    void create() {
        // given
        ShortestPath shortestPath = new ShortestPath(stations, sections);

        // when
        List<Station> result = shortestPath.findPath(station3, station1);

        // then
        List<Station> expected = List.of(station3, station2, station4, station1);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("거리 계산하기")
    @Test
    void calculateDistance() {
        // given
        ShortestPath shortestPath = new ShortestPath(stations, sections);

        // when
        int result = shortestPath.calculateDistance(station3, station1);

        // then
        assertThat(result).isEqualTo(5);
    }

    @DisplayName("거리 계산하기")
    @ParameterizedTest(name = "{0} km -> 요금 {1}원 예상")
    @CsvSource(value = {"5,1250", "44,1950", "60,2250"})
    void calculateScore2(int distance, int expected) {
        // given
        List<Station> stations = List.of(station1, station2);
        List<Section> sections = List.of(new Section(1L, 1L, station1.getId(), station2.getId(), distance));
        ShortestPath shortestPath = new ShortestPath(stations, sections);

        // when
        int result = shortestPath.calculateScore(station1, station2);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("순회하는 노선 목록 찾기")
    @Test
    void findLines() {
        ShortestPath shortestPath = new ShortestPath(stations, sections);
        List<Long> ids = shortestPath.findLineIds(station1, station2);

        assertThat(ids).contains(lineId1, lineId2);
    }
}
