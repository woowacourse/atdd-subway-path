package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Distance {

    BASIC(0, 0),
    MIDDLE(5, 10),
    FAR(8, 50);

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final int fareUnit;
    private final int distanceUnit;

    Distance(int fareUnit, int distanceUnit) {
        this.fareUnit = fareUnit;
        this.distanceUnit = distanceUnit;
    }

    public static List<Distance> findByDistance(int distance) {
        return Arrays.stream(Distance.values())
                .filter(it -> it.distanceUnit < distance)
                .sorted(Comparator.comparingInt(Distance::getDistanceUnit).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    public int calculateAdditionalFare(int distance) {
        if (BASIC == this) {
            return BASIC_FARE;
        }
        return ((distance - distanceUnit - 1) / fareUnit + 1) * EXTRA_FARE;
    }

    public int getDistanceUnit() {
        return distanceUnit;
    }
}
