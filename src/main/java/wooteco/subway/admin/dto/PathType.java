package wooteco.subway.admin.dto;

import java.util.Objects;
import java.util.function.Function;

import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.exception.IllegalTypeNameException;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private final Function<LineStation, Integer> function;

    PathType(Function<LineStation, Integer> function) {
        this.function = function;
    }

    public int getWeight(LineStation lineStation) {
        return function.apply(lineStation);
    }

    public static PathType of(String typeName) {
        String upperCaseName = typeName.toUpperCase();
        if (!Objects.equals(upperCaseName, DURATION.name()) && !Objects.equals(upperCaseName,
            DISTANCE.name())) {
            throw new IllegalTypeNameException(typeName);
        }
        return valueOf(upperCaseName);
    }
}
