package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 잠실역 = new Station("잠실역");
    private final Station 건대역 = new Station("건대역");

    private final List<Station> stations = Arrays.asList(강남역, 잠실역, 건대역);
    private final List<Section> sections = Arrays.asList(
            new Section(강남역, 잠실역, 100),
            new Section(강남역, 건대역, 20),
            new Section(잠실역, 건대역, 10)
    );

    @DisplayName("최단경로 찾아 List<Station>으로 반환")
    @Test
    void shortPath() {
        Path path = new Path(stations, sections);
        List<Station> shortPath = path.shortestPath(강남역, 잠실역);
        int distance = path.shortestDistance(강남역, 잠실역);

        assertThat(shortPath).containsExactly(강남역, 건대역, 잠실역);
        assertThat(distance).isEqualTo(30);
    }
}