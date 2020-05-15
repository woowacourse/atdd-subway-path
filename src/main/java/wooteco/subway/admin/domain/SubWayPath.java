package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.admin.exception.NotFoundPathException;

import java.util.List;
import java.util.Objects;

public class SubWayPath {
    private final GraphPath<Station, LineStationEdge> path;
    private final Station source;
    private final Station target;

    public SubWayPath(DijkstraShortestPath<Station, LineStationEdge> path, Station source, Station target) {
        this.source = source;
        this.target = target;
        this.path = validate(path.getPath(source, target));
    }

    private GraphPath<Station, LineStationEdge> validate(GraphPath<Station, LineStationEdge> graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new NotFoundPathException(source.getName(), target.getName());
        }

        return graphPath;
    }

    public List<Station> stations() {
        return path.getVertexList();
    }

    public int distance() {
        return (int) path.getEdgeList().stream()
                .mapToDouble(LineStationEdge::getDistance)
                .sum();
    }

    public int duration() {
        return (int) path.getEdgeList().stream()
                .mapToDouble(LineStationEdge::getDuration)
                .sum();
    }
}
