package wooteco.subway.application.path;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.path.Graph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class JGraphtAdapter implements Graph {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath;
    private final Map<Long, Station> vertex;

    public JGraphtAdapter(List<Station> vertex, List<Section> edge) {
        this.shortestPath = createDijkstraShortestPath(vertex, edge);
        this.vertex = createVertexMap(vertex);
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> createDijkstraShortestPath(
            List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }

        return new DijkstraShortestPath<>(graph);
    }

    private Map<Long, Station> createVertexMap(List<Station> vertex) {
        return vertex.stream()
                .collect(Collectors.toMap(Station::getId, value -> value));
    }

    @Override
    public List<Station> findPath(Long source, Long target) {
        GraphPath<Long, DefaultWeightedEdge> graph = shortestPath.getPath(source, target);

        if (graph == null) {
            return List.of();
        }

        return graph.getVertexList().stream()
                .map(vertex::get)
                .collect(Collectors.toList());
    }

    @Override
    public int findDistance(Long source, Long target) {
        return (int) shortestPath.getPathWeight(source, target);
    }
}
