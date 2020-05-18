package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;

public class SubwayPath {
    private static final int FIRST = 0;

    private final List<SubwayWeightEdge> paths;

    public SubwayPath(final GraphPath<Long, SubwayWeightEdge> path) {
        this.paths = path.getEdgeList();
    }

    public PathCost getCost() {
        Integer distance = 0;
        Integer duration = 0;
        for (SubwayWeightEdge subwayWeightEdge : paths) {
            distance += subwayWeightEdge.getValue(Edge::getDistance);
            duration += subwayWeightEdge.getValue(Edge::getDuration);
        }
        return new PathCost(distance, duration);
    }

    public List<Long> getPaths() {
        List<Long> paths = new ArrayList<>();

        paths.add(getFirstVertexId());
        for (SubwayWeightEdge path : this.paths) {
            paths.add(path.getStationId());
        }

        return paths;
    }

    private Long getFirstVertexId() {
        return paths.get(FIRST).getPreStationId();
    }
}
