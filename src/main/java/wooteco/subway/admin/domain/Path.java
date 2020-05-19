package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

public class Path {
    private GraphPath<Long, LineStationEdge> path;

    private Path(GraphPath<Long, LineStationEdge> path) {
        this.path = path;
    }

    public static Path of(Long sourceId, Long targetId, Graph<Long, LineStationEdge> graph,
        PathStrategy pathStrategy) {
        return new Path(pathStrategy.getPath(sourceId, targetId, graph));
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

    public List<Long> getVertexList() {
        return path.getVertexList();
    }
}
