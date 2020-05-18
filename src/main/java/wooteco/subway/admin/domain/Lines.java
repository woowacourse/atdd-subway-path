package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.dto.LineWithStationsResponse;
import wooteco.subway.admin.exception.WrongPathException;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Edge> findWholeEdges() {
        return lines.stream()
                .flatMap(line -> line.getEdgesExceptFirst().stream())
                .collect(Collectors.toList());
    }

    public List<Long> createShortestPath(Long sourceStationId, Long targetStationId, PathType type) {

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addAllVertex(graph);
        setAllEdgeWeight(graph, type);

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            return dijkstraShortestPath.getPath(sourceStationId, targetStationId).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new WrongPathException();
        }
    }

    private void addAllVertex(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        lines.stream()
                .flatMap(line -> line.getStationIds().stream())
                .forEach(graph::addVertex);
    }

    private void setAllEdgeWeight(WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathType type) {
        lines.stream()
                .flatMap(line -> line.getEdgesExceptFirst().stream())
                .forEach(edge
                        -> graph.setEdgeWeight(graph.addEdge(edge.getPreStationId(), edge.getStationId()), type.getWeight(edge)));
    }

    public List<Long> getStationIds() {
        return lines.stream()
                .flatMap(line -> line.getEdges().stream())
                .map(Edge::getStationId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<LineWithStationsResponse> findLineWithStationsResponses(Stations stations) {
        return lines.stream()
                .map(line -> LineWithStationsResponse.of(line, stations.filterStationsByIds(line.getSortedStationIds())))
                .collect(Collectors.toList());
    }
}
