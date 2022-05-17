package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PathInfoTest {

    private final List<Station> stations = List.of(
            new Station(1L, "v1"),
            new Station(2L, "v2"),
            new Station(3L, "v3"),
            new Station(4L, "v4"));

    private final List<Section> sections = List.of(
            new Section(1L, 1L, 1L, 4L, 1),
            new Section(2L, 2L, 1L, 4L, 2),
            new Section(3L, 1L, 4L, 2L, 2),
            new Section(4L, 1L, 2L, 3L, 2)
    );


    @DisplayName("최단 경로 찾아주는 객체를 초기화한다.")
    @Test
    void create() {
        // given
        PathInfo pathInfo = new PathInfo(stations, sections);

        // when
        List<Station> result = pathInfo.findPath(new Station(3L, "v3"), new Station(1L, "v1"));

        // then
        List<Station> expected = List.of(
                new Station(3L, "v3"),
                new Station(2L, "v2"),
                new Station(4L, "v4"),
                new Station(1L, "v1")
        );
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("거리 계산하기")
    @Test
    void calculateDistance() {
        // given
        PathInfo pathInfo = new PathInfo(stations, sections);

        // when
        int result = pathInfo.calculateMinDistance(new Station(3L, "v3"), new Station(1L, "v1"));

        // then
        assertThat(result).isEqualTo(5);
    }

    @DisplayName("거리 계산하기")
    @ParameterizedTest(name = "{0} km -> 요금 {1}원 예상")
    @CsvSource(value = {"5,1250", "44,1950", "60,2250"})
    void calculateScore2(int distance, int expected) {
        // given
        List<Station> stations = List.of(new Station(1L, "1"), new Station(2L, "2"));
        List<Section> sections = List.of(new Section(1L, 1L, 1L, 2L, distance));
        PathInfo pathInfo = new PathInfo(stations, sections);

        // when
        int result = pathInfo.calculateScore(new Station(1L, "1"), new Station(2L, "2"));

        // then
        assertThat(result).isEqualTo(expected);
    }
}
