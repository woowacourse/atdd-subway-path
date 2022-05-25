package wooteco.subway.domain.distance;

import java.util.Arrays;

public enum DistanceType {

    BASE_DISTANCE(1, 10),
    MIDDLE_DISTANCE(11, 50),
    LONG_DISTANCE(50, Integer.MAX_VALUE);

    private final int minValue;
    private final int maxValue;

    DistanceType(final int minValue, final int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static DistanceType from(final Distance distance) {
        return Arrays.stream(values())
                .filter(it -> distance.isBetween(it.minValue, it.maxValue))
                .findFirst()
                .orElseThrow();
    }
}
