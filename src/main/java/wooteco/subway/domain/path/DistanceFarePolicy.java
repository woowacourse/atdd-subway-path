package wooteco.subway.domain.path;

import java.util.Arrays;

public enum DistanceFarePolicy {

    DEFAULT_FARE(0, 10, 10, 1250),
    FIRST_LEVEL_FARE(10, 50, 5, 100),
    FINAL_LEVEL_FARE(50, Long.MAX_VALUE, 8, 100);

    private final long lowerLimit;
    private final long upperLimit;
    private final long unit;
    private final long rate;

    DistanceFarePolicy(long lowerLimit, long upperLimit, long unit, long rate) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.unit = unit;
        this.rate = rate;
    }

    public static long calculateByPolicy(long distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .mapToLong(farePolicy -> farePolicy.calculate(distance))
                .sum();
    }

    private long calculate(long distance) {
        if (distance <= lowerLimit) {
            return 0;
        }
        if (distance > upperLimit) {
            return (upperLimit - lowerLimit) / unit * rate;
        }
        return ((distance - lowerLimit - 1) / unit + 1) * rate;
    }

}
