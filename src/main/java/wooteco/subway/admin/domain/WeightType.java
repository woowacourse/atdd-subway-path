package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum WeightType {
    DISTANCE(lineStation -> Integer.toUnsignedLong(lineStation.getDistance())),
    DURATION(lineStation -> Integer.toUnsignedLong(lineStation.getDuration()));

    private final Function<LineStation, Long> weightFunction;

    WeightType(Function<LineStation, Long> weightFunction) {
        this.weightFunction = weightFunction;
    }

    public long getWeight(LineStation lineStation) {
        return weightFunction.apply(lineStation);
    }
}
