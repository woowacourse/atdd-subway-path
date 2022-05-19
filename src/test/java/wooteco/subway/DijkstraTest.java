package wooteco.subway;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;

public class DijkstraTest {

    private TestSections testSections = new TestSections();

    @Test
    void test() {
        testSections.findShortestPath(1L, 5L);
    }
}

class TestSections {

    private Section section1 = new Section(1L, 1L, 1L, 2L, 10);
    private Section section2 = new Section(2L, 1L, 2L, 3L, 12);
    private Section section3 = new Section(3L, 2L, 3L, 4L, 15);
    private Section section4 = new Section(4L, 2L, 4L, 5L, 20);

    private List<Section> sections = List.of(section1, section2, section3, section4);

    public Path findShortestPath(Long source, Long target) {
        WeightedMultigraph<Long, SubwayWeightedEdge> graph = new WeightedMultigraph<>(SubwayWeightedEdge.class);
        initPathGraph(graph, gatherStationIds());
        GraphPath<Long, SubwayWeightedEdge> path = new DijkstraShortestPath(graph).getPath(source, target);

        path.getEdgeList().forEach(it -> System.out.println(it.getLineId()));

        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private Set<Long> gatherStationIds() {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private void initPathGraph(WeightedMultigraph<Long, SubwayWeightedEdge> graph, Set<Long> ids) {
        for (Long id : ids) {
            graph.addVertex(id);
        }

        for (Section section : sections) {
            SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(section.getUpStationId(), section.getDownStationId());
            subwayWeightedEdge.setLineId(section.getLineId());
            graph.setEdgeWeight(
                    subwayWeightedEdge,
                    section.getDistance()
            );
        }
    }
}
