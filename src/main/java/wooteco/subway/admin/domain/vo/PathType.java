package wooteco.subway.admin.domain.vo;

import java.util.function.Function;

import wooteco.subway.admin.domain.LineStation;

public enum PathType {

    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> function;

    PathType(Function<LineStation, Integer> function) {
        this.function = function;
    }

    public static PathType of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch(IllegalArgumentException ie) {
            return DISTANCE;
        }
    }

    public int getWeight(LineStation lineStation) {
        return function.apply(lineStation);
    }
}
