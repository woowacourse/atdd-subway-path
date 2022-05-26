package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Distance {

    BASIC(0, 0, 9),
    MIDDLE(5, 10, 49),
    FAR(8, 50, Integer.MAX_VALUE);

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final int unit;
    private final int startPoint;
    private final int endPoint;

    Distance(int unit, int startPoint, int endPoint) {
        this.unit = unit;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public static List<Distance> findAvailableDistances(int distance) {
        return Arrays.stream(Distance.values())
                .filter(it -> it.startPoint < distance)
                .sorted(Comparator.comparingInt(Distance::getStartPoint).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    private int getStartPoint() {
        return startPoint;
    }

    public int calculateAdditionalFare(int distance) {
        if (BASIC == this) {
            return BASIC_FARE;
        }
        if (distance > endPoint) {
            return calculate(endPoint);
        }
        return calculate(distance);
    }

    private int calculate(int distance) {
        return ((distance - startPoint - 1) / unit + 1) * EXTRA_FARE;
    }
}
