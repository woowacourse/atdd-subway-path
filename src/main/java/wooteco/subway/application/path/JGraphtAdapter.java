package wooteco.subway.application.path;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Graph;

public class JGraphtAdapter implements Graph {

    private final DijkstraShortestPath<Long, Edge> shortestPath;
    private final Map<Long, Station> vertex;

    public JGraphtAdapter(List<Station> vertex, List<Section> edge) {
        this.shortestPath = createDijkstraShortestPath(vertex, edge);
        this.vertex = createVertexMap(vertex);
    }

    private class Edge extends DefaultWeightedEdge {
        private final Long lineId;
        private final Long source;
        private final Long target;

        public Edge(Long lineId, Long source, Long target) {
            this.lineId = lineId;
            this.source = source;
            this.target = target;
        }
    }

    private DijkstraShortestPath<Long, Edge> createDijkstraShortestPath(
            List<Station> stations, List<Section> sections) {

        WeightedMultigraph<Long, Edge> graph
                = new WeightedMultigraph<>(Edge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        for (Section section : sections) {
            Edge edge = new Edge(section.getLineId(), section.getUpStationId(), section.getDownStationId());
            graph.addEdge(section.getUpStationId(), section.getDownStationId(), edge);
            graph.setEdgeWeight(edge, section.getDistance());
        }

        return new DijkstraShortestPath<>(graph);
    }

    private Map<Long, Station> createVertexMap(List<Station> vertex) {
        return vertex.stream()
                .collect(Collectors.toMap(Station::getId, value -> value));
    }

    @Override
    public List<Station> findPath(Long source, Long target) {
        GraphPath<Long, Edge> graph = shortestPath.getPath(source, target);

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

    @Override
    public List<Long> findLineIdsRelatedPath(Long source, Long target) {
        GraphPath<Long, Edge> graph = shortestPath.getPath(source, target);

        return graph.getEdgeList()
                .stream()
                .map(edge -> edge.lineId)
                .distinct()
                .collect(Collectors.toList());
    }
}
