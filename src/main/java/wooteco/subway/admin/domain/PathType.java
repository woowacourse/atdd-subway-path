package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {

    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> function;

    PathType(Function<LineStation, Integer> function) {
        this.function = function;
    }

    public static PathType of(String name) {
        return valueOf(name.toUpperCase());
    }

    public int getWeight(LineStation lineStation) {
        return function.apply(lineStation);
    }
}
