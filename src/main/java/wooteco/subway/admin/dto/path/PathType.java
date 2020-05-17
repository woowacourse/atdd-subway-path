package wooteco.subway.admin.dto.path;

import wooteco.subway.admin.domain.LineStation;

import java.util.function.Function;

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
