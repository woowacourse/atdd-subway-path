package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SubwayPath {
    private static final int FIRST = 0;

    private final List<SubwayWeightEdge> paths;

    public SubwayPath(final GraphPath<Long, SubwayWeightEdge> path) {
        this.paths = path.getEdgeList();

    }

    public Integer sumOfEdge(Function<Edge, Integer> edgeIntegerFunction) {
        return paths.stream()
                .mapToInt(path -> path.getValue(edgeIntegerFunction))
                .sum();
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
