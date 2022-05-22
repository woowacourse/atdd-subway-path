package wooteco.subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayGraph;

public class JGraphtSubwayGraph implements SubwayGraph {

    private static class JGraphtSectionEdge extends DefaultWeightedEdge {

        private Section section;

        public JGraphtSectionEdge(Section section) {
            this.section = section;
        }

        public Section getSection() {
            return section;
        }
    }

    private final DijkstraShortestPath<Station, JGraphtSectionEdge> shortestPath;

    public JGraphtSubwayGraph(List<Station> stations, List<Section> sections) {
        this.shortestPath = createShortestPath(createStationsMap(stations), sections);
    }

    private Map<Long, Station> createStationsMap(List<Station> stations) {
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, value -> value));
    }

    private DijkstraShortestPath<Station, JGraphtSectionEdge> createShortestPath(
        Map<Long, Station> stations, List<Section> sections) {
        WeightedMultigraph<Station, JGraphtSectionEdge> graph
            = new WeightedMultigraph<>(JGraphtSectionEdge.class);

        for (Station station : stations.values()) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            Station upStation = stations.get(section.getUpStationId());
            Station downStation = stations.get(section.getDownStationId());
            JGraphtSectionEdge edge = new JGraphtSectionEdge(section);
            graph.addEdge(upStation, downStation, edge);
            graph.setEdgeWeight(edge, section.getDistance());
        }

        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public Path search(Station source, Station target) {
        GraphPath<Station, JGraphtSectionEdge> path = shortestPath.getPath(source, target);
        if (path == null) {
            return Path.EMPTY;
        }
        List<Section> sections = path.getEdgeList().stream()
            .map(JGraphtSectionEdge::getSection)
            .collect(Collectors.toList());
        return new Path(path.getVertexList(), sections, (int) path.getWeight());
    }
}
