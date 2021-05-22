package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final List<Station> stations;
    private final PathStrategy pathStrategy;

    public Path(List<Station> stations, List<Section> sections, PathStrategy pathStrategy) {
        this.stations = new ArrayList<>(stations);
        this.pathStrategy = pathStrategy;
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public GraphPath calculateShortestPath(Long sourceId, Long targetId) {
        return this.pathStrategy.calculateShortestPath(graph, stations, sourceId, targetId);
    }
}
