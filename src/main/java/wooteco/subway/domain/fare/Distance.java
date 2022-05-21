package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum Distance {
    BASE_RANGE(Distance::isBaseRange, distance -> Distance.BASE_FARE),
    MIDDLE_RANGE(Distance::isMiddleRange, Distance::calculateFareOnMiddleRange),
    LONG_RANGE(Distance::isLongRange, Distance::calculateFareOnLongRange);

    private static final int BASE_FARE = 1250;
    private static final int FARE_STEP = 100;

    private static final int MIDDLE_RANGE_BOUND = 10;
    private static final int LONG_RANGE_BOUND = 50;

    private static final int DISTANCE_PER_FARE_STEP_AT_MIDDLE_RANGE = 5;
    private static final int DISTANCE_PER_FARE_STEP_AT_LONG_RANGE = 8;

    private final Predicate<Integer> distanceCondition;
    private final Function<Integer, Integer> farePolicy;

    Distance(Predicate<Integer> distanceCondition, Function<Integer, Integer> farePolicy) {
        this.distanceCondition = distanceCondition;
        this.farePolicy = farePolicy;
    }

    public static int calculateFareBy(int distance) {
        return Arrays.stream(Distance.values())
                .filter(range -> range.distanceCondition.test(distance))
                .map(range -> range.farePolicy.apply(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 거리 구간이 없습니다."));

    }

    private static boolean isBaseRange(int distance) {
        return distance <= MIDDLE_RANGE_BOUND;
    }

    private static boolean isMiddleRange(int distance) {
        return MIDDLE_RANGE_BOUND < distance
                && distance <= LONG_RANGE_BOUND;
    }

    private static boolean isLongRange(int distance) {
        return LONG_RANGE_BOUND < distance;
    }

    private static int calculateFareOnMiddleRange(int distance) {
        return BASE_FARE
                + ((distance - MIDDLE_RANGE_BOUND - 1) / DISTANCE_PER_FARE_STEP_AT_MIDDLE_RANGE + 1) * FARE_STEP;
    }

    private static int calculateFareOnLongRange(int distance) {
        return BASE_FARE
                + (LONG_RANGE_BOUND - MIDDLE_RANGE_BOUND) / DISTANCE_PER_FARE_STEP_AT_MIDDLE_RANGE * FARE_STEP
                + ((distance - LONG_RANGE_BOUND - 1) / DISTANCE_PER_FARE_STEP_AT_LONG_RANGE + 1) * FARE_STEP;
    }
}
