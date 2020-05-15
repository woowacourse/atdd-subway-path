package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.dto.GraphResponse;
import wooteco.subway.admin.dto.PathType;
import wooteco.subway.admin.exception.IllegalStationNameException;
import wooteco.subway.admin.exception.NotFoundLineException;
import wooteco.subway.admin.exception.NotFoundPathException;

public class Graph {

    private final WeightedMultigraph<Long, LineStationEdge> graph;

    private Graph(
        WeightedMultigraph<Long, LineStationEdge> graph) {
        this.graph = graph;
    }

    public static Graph of(List<Line> lines, PathType pathType) {
        return new Graph(mapLinesToGraph(lines, pathType));
    }

    private static WeightedMultigraph<Long, LineStationEdge> mapLinesToGraph(List<Line> lines,
        PathType pathType) {
        if (Objects.isNull(lines)) {
            throw new NotFoundLineException();
        }
        WeightedMultigraph<Long, LineStationEdge> graph
            = new WeightedMultigraph(LineStationEdge.class);
        lines.stream()
            .flatMap(it -> it.getLineStationsId().stream())
            .forEach(graph::addVertex);
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .filter(it -> Objects.nonNull(it.getPreStationId()))
            .forEach(
                it -> graph.addEdge(it.getPreStationId(), it.getStationId(),
                    new LineStationEdge(it, pathType)));
        return graph;
    }

    public GraphResponse findPath(Long sourceId, Long targetId) {
        validate(sourceId, targetId);
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);

        if (Objects.isNull(dijkstraShortestPath.getPath(sourceId, targetId))) {
            throw new NotFoundPathException(sourceId, targetId);
        }

        return mapToGraphResponse(sourceId, targetId, dijkstraShortestPath);
    }

    private void validate(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalStationNameException(sourceId, targetId);
        }
    }

    private GraphResponse mapToGraphResponse(Long source, Long target,
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath) {
        List<Long> path = dijkstraShortestPath.getPath(source, target).getVertexList();
        int totalDistance = dijkstraShortestPath.getPath(source, target)
            .getEdgeList()
            .stream()
            .mapToInt(LineStationEdge::getDistance)
            .sum();
        int totalDuration = dijkstraShortestPath.getPath(source, target)
            .getEdgeList()
            .stream()
            .mapToInt(LineStationEdge::getDuration)
            .sum();
        return new GraphResponse(path, totalDistance, totalDuration);
    }
}