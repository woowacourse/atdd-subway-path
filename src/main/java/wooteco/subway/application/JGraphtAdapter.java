package wooteco.subway.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Graph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class JGraphtAdapter implements Graph {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath;
    private final Map<Long, Station> stations;

    public JGraphtAdapter(List<Station> stations, List<Section> sections) {
        this.shortestPath = createShortestPath(stations, sections);
        this.stations = createVertexMap(stations);
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> createShortestPath(
        List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        for (Section section : sections) {
            DefaultWeightedEdge edge = graph.addEdge(
                section.getUpStationId(), section.getDownStationId());
            graph.setEdgeWeight(edge, section.getDistance());
        }

        return new DijkstraShortestPath<>(graph);
    }

    private Map<Long, Station> createVertexMap(List<Station> vertex) {
        return vertex.stream()
            .collect(Collectors.toMap(Station::getId, value -> value));
    }

    @Override
    public Path search(Long source, Long target) {
        GraphPath<Long, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if (isUnreachablePath(path)) {
            return new Path(List.of(), 0);
        }
        return new Path(createStations(path), (int) path.getWeight());
    }

    private boolean isUnreachablePath(GraphPath<Long, DefaultWeightedEdge> path) {
        return path == null;
    }

    private List<Station> createStations(GraphPath<Long, DefaultWeightedEdge> path) {
        return path.getVertexList().stream()
            .map(stations::get)
            .collect(Collectors.toList());
    }
}
