package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
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

public class ShortestPathCalculatorTest {

    @DisplayName("모든 구간을 생성하여 최소거리의 역을 계산한다.")
    @Test
    void findShortestStations() {
        ShortestPathCalculator shortestPathCalculator = getShortestPathCalculator();
        shortestPathCalculator.initializeGraph(createSections());

        List<Station> shortestStations = shortestPathCalculator.calculateShortestStations(신당역, 창신역);
        assertThat(shortestStations).hasSize(3);
    }

    @DisplayName("모든 구간을 생성하여 최소거리를 계산한다.")
    @Test
    void calculateShortestDistance() {
        ShortestPathCalculator shortestPathCalculator = getShortestPathCalculator();
        shortestPathCalculator.initializeGraph(createSections());

        int distance = shortestPathCalculator.calculateShortestDistance(신당역, 창신역);
        assertThat(distance).isEqualTo(20);
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
