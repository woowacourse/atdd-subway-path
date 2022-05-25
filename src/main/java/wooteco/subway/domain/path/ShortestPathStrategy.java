package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class ShortestPathStrategy implements PathStrategy {

    private final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;

    public ShortestPathStrategy(List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        setVertex(graph, sections);
        setEdgeWeight(graph, sections);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void setVertex(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        sections.forEach((section) -> {
            graph.addVertex(section.getDownStation());
            graph.addVertex(section.getUpStation());
        });
    }

    private void setEdgeWeight(WeightedMultigraph<Station, SectionEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            Station downStation = section.getDownStation();
            Station upStation = section.getUpStation();
            SectionEdge sectionEdge = new SectionEdge(section.getLineId(), section.getDistance());

            graph.addEdge(downStation, upStation, sectionEdge);
        }
    }

    public List<Station> findPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int calculateDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public List<Long> findLineIds(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getEdgeList()
                .stream()
                .map(SectionEdge::getLineId)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    private static class SectionEdge extends DefaultWeightedEdge {

        private final Long lineId;
        private final int distance;

        public SectionEdge(Long lineId, int distance) {
            this.lineId = lineId;
            this.distance = distance;
        }

        public Long getLineId() {
            return lineId;
        }

        @Override
        protected double getWeight() {
            return distance;
        }
    }
}
