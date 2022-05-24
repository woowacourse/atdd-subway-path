package wooteco.subway.domain.path.cost;

import java.util.Arrays;

public enum DistanceSection {

    OVER_FARE_ONE(10, 50, 100, 5),
    OVER_FARE_TWO(50, Integer.MAX_VALUE, 100, 8);

    private static final int BASIC_FARE = 1250;
    private final int lowerBound;
    private final int upperBound;
    private final int fare;
    private final int perDistance;

    DistanceSection(int lowerBound, int upperBound, int fare, int perDistance) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.fare = fare;
        this.perDistance = perDistance;
    }

    private boolean isContain(int distance) {
        return distance > lowerBound;
    }

    private int calculateMaxFare() {
        return (int) Math.ceil((upperBound - lowerBound) / perDistance) * fare;
    }

    private int calculateOverFare(int distance) {
        int faredDistance = distance - lowerBound;
        return (int) ((Math.ceil((faredDistance - 1) / perDistance) + 1) * fare);
    }

    public static int calculateByDistance(int distance) {
        if (distance <= 0) {
            return 0;
        }
        return Arrays.stream(DistanceSection.values()).filter(eachSection -> eachSection.isContain(distance))
                .mapToInt(eachSection -> Math.min(eachSection.calculateOverFare(distance),
                        eachSection.calculateMaxFare()))
                .sum() + BASIC_FARE;
    }
}
