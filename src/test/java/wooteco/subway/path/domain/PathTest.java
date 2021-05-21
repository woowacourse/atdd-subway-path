package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {
    @Test
    @DisplayName("최단거리 알고리즘 확인")
    void path() {
        Station 강남역 = new Station("강남역");
        Station 잠실역 = new Station("잠실역");
        Station 구의역 = new Station("구의역");

        Section section1 = new Section(강남역, 잠실역, 2);
        Section section2 = new Section(잠실역, 구의역, 2);
        Section section3 = new Section(강남역, 구의역, 100);

        Path path = new Path(Arrays.asList(강남역, 잠실역, 구의역), Arrays.asList(section1, section2, section3));

        List<Station> shortestPath = path.shortestPath(강남역, 구의역);
        assertThat(shortestPath).containsExactly(강남역, 잠실역, 구의역);

        double shortestPathLength = path.shortestPathLength(강남역, 구의역);
        assertThat(shortestPathLength).isEqualTo(4);
    }
}
