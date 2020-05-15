package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.GraphResponse;
import wooteco.subway.admin.dto.PathType;
import wooteco.subway.admin.exception.IllegalStationNameException;
import wooteco.subway.admin.exception.NotFoundLineException;
import wooteco.subway.admin.exception.NotFoundPathException;

@Service
public class GraphService {
    public GraphResponse findPath(List<Line> lines, Long sourceId, Long targetId,
        PathType pathType) {
        validate(lines, sourceId, targetId);
        WeightedMultigraph<Long, LineStationEdge> graph = mapLinesToGraph(lines, pathType);
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);

        if (Objects.isNull(dijkstraShortestPath.getPath(sourceId, targetId))) {
            throw new NotFoundPathException(sourceId, targetId);
        }

        return mapToGraphResponse(sourceId, targetId, dijkstraShortestPath);
    }

    private void validate(List<Line> lines, Long sourceId, Long targetId) {
        if (Objects.isNull(lines)) {
            throw new NotFoundLineException();
        }
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalStationNameException(sourceId, targetId);
        }
    }

    private WeightedMultigraph<Long, LineStationEdge> mapLinesToGraph(List<Line> lines,
        PathType pathType) {
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