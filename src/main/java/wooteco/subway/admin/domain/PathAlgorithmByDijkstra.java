package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;

import wooteco.subway.admin.exception.IllegalStationNameException;
import wooteco.subway.admin.exception.NotFoundPathException;

@Component
public class PathAlgorithmByDijkstra implements PathAlgorithm {

    public PathResult findPath(Long sourceId, Long targetId, Graph graph) {
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(graph.getGraph());
        validate(sourceId, targetId);

        if (Objects.isNull(dijkstraShortestPath.getPath(sourceId, targetId))) {
            throw new NotFoundPathException(sourceId, targetId);
        }

        return mapToPathResult(sourceId, targetId, dijkstraShortestPath);
    }

    private void validate(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalStationNameException(sourceId, targetId);
        }
    }

    private PathResult mapToPathResult(Long source, Long target,
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath) {
        List<Long> path = dijkstraShortestPath.getPath(source, target).getVertexList();
        int totalDistance = 0;
        int totalDuration = 0;

        for (LineStationEdge edge : dijkstraShortestPath.getPath(source, target).getEdgeList()) {
            totalDistance += edge.getDistance();
            totalDuration += edge.getDuration();
        }

        return new PathResult(path, totalDistance, totalDuration);
    }
}
