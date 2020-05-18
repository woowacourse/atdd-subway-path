package wooteco.subway.admin.domain.graph;

import wooteco.subway.admin.domain.Edge;

import java.util.ArrayList;
import java.util.List;

public class SubwayPath {
    private static final int FIRST = 0;

    private final List<WeightEdge> paths;

    public SubwayPath(final List<WeightEdge> paths) {
        this.paths = paths;
    }

    public PathCost getCost() {
        Integer distance = 0;
        Integer duration = 0;
        for (WeightEdge subwayWeightEdge : paths) {
            distance += subwayWeightEdge.getValue(Edge::getDistance);
            duration += subwayWeightEdge.getValue(Edge::getDuration);
        }
        return new PathCost(distance, duration);
    }

    public List<Long> getPaths() {
        List<Long> paths = new ArrayList<>();

        paths.add(getFirstVertexId());
        for (WeightEdge path : this.paths) {
            paths.add(path.getStationId());
        }

        return paths;
    }

    private Long getFirstVertexId() {
        return paths.get(FIRST).getPreStationId();
    }
}
