package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(PathEdge::getDistance),
    DURATION(PathEdge::getDuration);

    Function<PathEdge, Integer> weight;

    PathType(Function<PathEdge, Integer> weight) {
        this.weight = weight;
    }

    public int getWeight(PathEdge pathEdge) {
        return weight.apply(pathEdge);
    }
}
