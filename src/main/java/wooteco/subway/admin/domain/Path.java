package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;

import java.util.List;

public class Path {
    private GraphPath<Long, LineStationEdge> path;

    private Path(GraphPath<Long, LineStationEdge> path) {
        this.path = path;
    }

    public static Path of(Long sourceId, Long targetId, ShortestPathAlgorithm<Long, LineStationEdge> graph) {
        return new Path(graph.getPath(sourceId, targetId));
    }

    public List<Long> getVertexList() {
        return path.getVertexList();
    }

    public int totalDuration() {
        return path.getEdgeList()
                .stream()
                .mapToInt(LineStationEdge::getDuration)
                .sum();
    }

    public int totalDistance() {
        return path.getEdgeList()
                .stream()
                .mapToInt(LineStationEdge::getDistance)
                .sum();
    }
}
