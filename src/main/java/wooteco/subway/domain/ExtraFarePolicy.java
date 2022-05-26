package wooteco.subway.domain;

import java.util.Arrays;

public enum ExtraFarePolicy {
    DEFAULT_DISTANCE(0, 10, 1, 0, 1250),
    FIRST_EXTRA_DISTANCE(10, 50, 5, 100, 0),
    SECOND_EXTRA_DISTANCE(50, Integer.MAX_VALUE, 8, 100, 0);

    private int lowerDistance;
    private int upperDistance;
    private int perKm;
    private int perFare;
    private int basicFare;

    ExtraFarePolicy(int lowerDistance, int upperDistance, int perKm, int perFare, int basicFare) {
        this.lowerDistance = lowerDistance;
        this.upperDistance = upperDistance;
        this.perKm = perKm;
        this.perFare = perFare;
        this.basicFare = basicFare;
    }

    public static int findFare(double distance) {
        return Arrays.stream(ExtraFarePolicy.values())
                .mapToInt(extraFarePolicy -> extraFarePolicy.calculate(distance))
                .sum();
    }

    private int calculate(double distance) {
        if (isNoMoreDistance(distance)) {
            return 0;
        }
        if (isDistanceLeft(distance)) {
            return (int) (Math.ceil(((double) upperDistance - lowerDistance) / perKm) * perFare) + basicFare;
        }
        return (int) (Math.ceil((distance - lowerDistance) / perKm) * perFare) + basicFare;
    }

    private boolean isDistanceLeft(double distance) {
        return distance > upperDistance;
    }

    private boolean isNoMoreDistance(double distance) {
        return distance < lowerDistance;
    }
}
