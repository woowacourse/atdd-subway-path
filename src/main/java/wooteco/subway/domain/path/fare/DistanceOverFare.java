package wooteco.subway.domain.path.fare;

import java.util.Objects;

public class DistanceOverFare {

    private static final int OVER_FARE_DIGIT = 100;
    private static final int FIRST_OVER_FARE_AREA_THRESHOLD = 10;
    private static final int FIRST_OVER_FARE_DISTANCE_LIMIT = 5;
    private static final int SECOND_OVER_FARE_AREA_THRESHOLD = 50;
    private static final int SECOND_OVER_FARE_DISTANCE_LIMIT = 8;

    private final int value;

    DistanceOverFare(int value) {
        this.value = value;
    }

    public static DistanceOverFare of(int distance) {
        return new DistanceOverFare(calculateOverFare(distance));
    }

    private static int calculateOverFare(int totalDistance) {
        int firstAreaOverFare = calculateFirstAreaOverFare(totalDistance);
        int secondAreaOverFare = calculateSecondAreaOverFare(totalDistance);
        return firstAreaOverFare + secondAreaOverFare;
    }

    private static int calculateFirstAreaOverFare(int totalDistance) {
        if (totalDistance <= FIRST_OVER_FARE_AREA_THRESHOLD) {
            return 0;
        }
        int distance = Math.min(totalDistance, SECOND_OVER_FARE_AREA_THRESHOLD);
        int overDistance = distance - FIRST_OVER_FARE_AREA_THRESHOLD;
        return toOverFare(overDistance, FIRST_OVER_FARE_DISTANCE_LIMIT);
    }

    private static int calculateSecondAreaOverFare(int totalDistance) {
        if (totalDistance <= SECOND_OVER_FARE_AREA_THRESHOLD) {
            return 0;
        }
        int overDistance = totalDistance - SECOND_OVER_FARE_AREA_THRESHOLD;
        return toOverFare(overDistance, SECOND_OVER_FARE_DISTANCE_LIMIT);
    }

    private static int toOverFare(int overDistance, int limit) {
        double overDigit = Math.ceil((overDistance - 1) / limit) + 1;
        return (int) (overDigit * OVER_FARE_DIGIT);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistanceOverFare that = (DistanceOverFare) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "DistanceOverFare{" + "value=" + value + '}';
    }
}
