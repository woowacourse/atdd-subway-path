package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DURATION(Edge::getDuration),
    DISTANCE(Edge::getDistance);

    private final Function<Edge, Integer> weight;

    PathType(Function<Edge, Integer> weight) {
        this.weight = weight;
    }

    public int getWeight(Edge edge) {
        return this.weight.apply(edge);
    }
}
