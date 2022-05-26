package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.TestFixtures.동묘앞역;
import static wooteco.subway.TestFixtures.분당선_6호선_노선;
import static wooteco.subway.TestFixtures.신당역;
import static wooteco.subway.TestFixtures.창신역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

class PathTest {

    private Path path = new Path(신당역, 창신역, DijkstraShortestGraphAlgorithm.generate(createSections()));

    @DisplayName("출발역부터 도착역까지의 리스트를 찾는다.")
    @Test
    void findEdges() {
        List<ShortestPathEdge> edges = path.findEdges();
        assertAll(() -> {
            assertThat(edges).hasSize(2);
            assertThat(edges.get(0).getLineId()).isEqualTo(1L);
            assertThat(edges.get(1).getLineId()).isEqualTo(1L);
        });
    }

    @DisplayName("출발역부터 도착역까지의 최단 거리를 찾는다.")
    @Test
    void calculateMinDistance() {
        assertThat(path.calculateMinDistance()).isEqualTo(STANDARD_DISTANCE + STANDARD_DISTANCE);
    }

    @DisplayName("출발역부터 도착역까지의 역들을 찾는다.")
    @Test
    void findShortestStations() {
        List<Station> shortestStations = path.findShortestStations();
        assertThat(shortestStations).containsExactly(신당역, 동묘앞역, 창신역);
    }

    @DisplayName("출발역부터 도착역까지의 추가 노선 요금을 구한다.")
    @Test
    void findMaxExtraLineFare() {
        assertThat(path.findMaxExtraLineFare(분당선_6호선_노선)).isEqualTo(0);
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }
}
