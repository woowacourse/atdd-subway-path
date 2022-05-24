package wooteco.subway.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Distance {

    FIRST(5, 10),
    SECOND(8, 50);

    private static final int EXTRA_FARE = 100;

    private final int fareUnit;
    private final int distanceUnit;

    Distance(int fareUnit, int distanceUnit) {
        this.fareUnit = fareUnit;
        this.distanceUnit = distanceUnit;
    }

    public static List<Distance> sortedByDistanceUnit() {
        return Arrays.stream(Distance.values())
                .sorted(Comparator.comparingInt(distance -> distance.distanceUnit))
                .collect(Collectors.toUnmodifiableList());
    }

    public int calculate(int distance) {
        if (distance <= distanceUnit) {
            return 0;
        }
        return ((distance - distanceUnit - 1) / fareUnit + 1) * EXTRA_FARE;
    }

    public int getDistanceUnit() {
        return distanceUnit;
    }
}
