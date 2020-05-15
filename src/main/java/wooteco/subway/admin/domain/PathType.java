package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.InvalidPathTypeException;

import java.util.Arrays;
import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private final Function<LineStation, Integer> weightStrategy;

    PathType(Function<LineStation, Integer> weightStrategy) {
        this.weightStrategy = weightStrategy;
    }

    public static PathType of(String input) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(input))
                .findAny()
                .orElseThrow(InvalidPathTypeException::new);
    }

    public Integer weight(LineStation lineStation) {
        return weightStrategy.apply(lineStation);
    }
}
