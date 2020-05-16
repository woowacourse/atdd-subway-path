package wooteco.subway.admin.domain.path;

import java.util.function.Function;
import wooteco.subway.admin.domain.Edge;

public enum PathType {
    DISTANCE(Edge::getDistance, Edge::getDuration),
    DURATION(Edge::getDuration, Edge::getDistance);

    private final Function<Edge, Integer> findWeight;
    private final Function<Edge, Integer> findSubWeight;

    PathType(Function<Edge, Integer> findWeight, Function<Edge, Integer> findSubWeight) {
        this.findWeight = findWeight;
        this.findSubWeight = findSubWeight;
    }

    public static PathType of(String type) {
        try {
            return PathType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("경로 찾기의 Type이 잘못되었습니다.");
        }
    }

    public int findWeight(Edge edge) {
        return findWeight.apply(edge);
    }

    public int findSubWeight(Edge edge) {
        return findSubWeight.apply(edge);
    }

    public int getDistance(int weight, int subWeight) {
        if (this == DURATION) {
            return subWeight;
        }
        return weight;
    }

    public int getDuration(int weight, int subWeight) {
        if (this == DISTANCE) {
            return subWeight;
        }
        return weight;
    }
}
