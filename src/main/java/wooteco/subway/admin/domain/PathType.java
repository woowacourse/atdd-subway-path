package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DURATION(lineStation -> lineStation.getDuration()),
    DISTANCE(lineStation -> lineStation.getDistance());

    private Function<LineStation, Integer> weight;

    PathType(Function<LineStation, Integer> weight) {
        this.weight = weight;
    }

    public int getWeight(LineStation lineStation) {
        return this.weight.apply(lineStation);
    }

    public boolean isDuration() {
        return this == PathType.DURATION;
    }

    public boolean isDistance() {
        return this == PathType.DISTANCE;
    }
}
