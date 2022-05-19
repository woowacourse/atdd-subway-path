package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.TestFixtures.동묘앞역;
import static wooteco.subway.TestFixtures.신당역;
import static wooteco.subway.TestFixtures.창신역;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.SubwayException;

class PathTest {

    @DisplayName("최소 거리를 계산한다.")
    @Test
    void calculateMinDistance() {
        ShortestPathCalculator shortestPathCalculator = getShortestPathCalculator();
        shortestPathCalculator.initializeGraph(createSections());

        Path path = new Path(shortestPathCalculator, 신당역, 창신역);
        assertThat(path.calculateMinDistance()).isEqualTo(20);
    }

    @DisplayName("최소 거리에 속한 역을 반환한다.")
    @Test
    void findShortestStations() {
        ShortestPathCalculator shortestPathCalculator = getShortestPathCalculator();
        shortestPathCalculator.initializeGraph(createSections());

        Path path = new Path(shortestPathCalculator, 신당역, 창신역);
        assertThat(path.findShortestStations()).hasSize(3);
    }

    @DisplayName("동일한 역이 들어올 시 예외가 발생한다.")
    @Test
    void validateSameStations() {
        ShortestPathCalculator shortestPathCalculator = getShortestPathCalculator();
        shortestPathCalculator.initializeGraph(createSections());

        assertThatThrownBy(() -> new Path(shortestPathCalculator, 신당역, 신당역))
                .isInstanceOf(SubwayException.class);
    }

    private ShortestPathCalculator getShortestPathCalculator() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        return new ShortestPathCalculator(graph, new DijkstraShortestPath<>(graph));
    }

    private Sections createSections() {
        Section section = new Section(1L, 1L, 신당역, 동묘앞역, STANDARD_DISTANCE);
        Section section1 = new Section(2L, 1L, 동묘앞역, 창신역, STANDARD_DISTANCE);
        return new Sections(List.of(section, section1));
    }

}
