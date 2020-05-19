package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum PathType {
    DISTANCE(lineStation -> Integer.toUnsignedLong(lineStation.getDistance())),
    DURATION(lineStation -> Integer.toUnsignedLong(lineStation.getDuration()));

    private final Function<LineStation, Long> weightFunction;

    PathType(Function<LineStation, Long> weightFunction) {
        this.weightFunction = weightFunction;
    }

    public static PathType of(String name) {
        return Arrays.stream(values())
                .filter(pathType -> pathType.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 PathType 입니다."));
    }

    public long getWeight(LineStation lineStation) {
        return weightFunction.apply(lineStation);
    }

    private boolean isSameName(String name) {
        return name().equals(name);
    }
}
