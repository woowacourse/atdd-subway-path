package wooteco.subway.admin.dto;

import java.util.function.Function;

import wooteco.subway.admin.domain.LineStation;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private final Function<LineStation, Integer> function;

    PathType(
        Function<LineStation, Integer> function) {
        this.function = function;
    }

    public int getWeight(LineStation lineStation){
        return function.apply(lineStation);
    }

    public static PathType of(String typeName){
        return valueOf(typeName.toUpperCase());
    }
}
