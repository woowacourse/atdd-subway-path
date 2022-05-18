package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShortPathStrategyTest {

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
        PathStrategy strategy = new ShortPathStrategy();

        // when
        List<Station> result = strategy.findPath(stations, sections, new Station(3L, "v3"), new Station(1L, "v1"));

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
        PathStrategy strategy = new ShortPathStrategy();

        // when
        int result = strategy.calculateDistance(stations, sections, new Station(3L, "v3"), new Station(1L, "v1"));

        // then
        assertThat(result).isEqualTo(5);
    }
}
