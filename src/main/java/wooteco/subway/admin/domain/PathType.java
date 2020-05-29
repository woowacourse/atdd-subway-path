package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DURATION(LineStation::getDuration),
    DISTANCE(LineStation::getDistance);

    private final Function<LineStation, Integer> weight;

    PathType(Function<LineStation, Integer> weight) {
        this.weight = weight;
    }

    public int getWeight(LineStation lineStation) {
        return this.weight.apply(lineStation);
    }
}
