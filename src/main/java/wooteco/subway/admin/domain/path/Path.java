package wooteco.subway.admin.domain.path;

import org.jgrapht.GraphPath;
import wooteco.subway.admin.domain.CustomException;

import java.util.List;
import java.util.Objects;

public class Path {
    private final GraphPath<Long, WeightedEdge> path;
    private final PathType pathType;

    public Path(GraphPath<Long, WeightedEdge> path, PathType pathType) {
        if (Objects.isNull(path)) {
            throw new CustomException("경로가 존재하지 않습니다.", new NullPointerException());
        }
        this.path = path;
        this.pathType = pathType;
    }

    public List<Long> getVertexList() {
        return path.getVertexList();
    }

    private int getWeight() {
        return (int) path.getWeight();
    }

    private int getSubWeight() {
        return path.getEdgeList()
                .stream()
                .mapToInt(WeightedEdge::getSubWeight)
                .sum();
    }

    public int getDistance() {
        if (pathType.equals(PathType.DISTANCE)) {
            return getWeight();
        }
        return getSubWeight();
    }

    public int getDuration() {
        if (pathType.equals(PathType.DURATION)) {
            return getWeight();
        }
        return getSubWeight();
    }
}
