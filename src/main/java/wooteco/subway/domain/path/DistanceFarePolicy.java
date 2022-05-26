package wooteco.subway.domain.path;

import static wooteco.subway.domain.path.DistanceFarePolicy.Constants.BASIC_CHARGE;
import static wooteco.subway.domain.path.DistanceFarePolicy.Constants.FIRST_ADDITIONAL_INTERVAL;
import static wooteco.subway.domain.path.DistanceFarePolicy.Constants.FIRST_INTERVAL_UNIT;
import static wooteco.subway.domain.path.DistanceFarePolicy.Constants.OVER_FARE;
import static wooteco.subway.domain.path.DistanceFarePolicy.Constants.SECOND_ADDITIONAL_INTERVAL;
import static wooteco.subway.domain.path.DistanceFarePolicy.Constants.SECOND_INTERVAL_UNIT;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DistanceFarePolicy {
    BASIC(DistanceFarePolicy::isDefaultInterval, distance -> BASIC_CHARGE),
    FIRST_INTERVAL(DistanceFarePolicy::isFirstInterval, DistanceFarePolicy::calculateFirstIntervalFare),
    SECOND_INTERVAL(DistanceFarePolicy::isSecondInterval, DistanceFarePolicy::calculateSecondIntervalFare);

    private final Predicate<Long> distanceCondition;

    private final UnaryOperator<Long> distancePolicy;

    DistanceFarePolicy(Predicate<Long> distanceCondition, UnaryOperator<Long> distancePolicy) {
        this.distanceCondition = distanceCondition;
        this.distancePolicy = distancePolicy;
    }

    private static boolean isDefaultInterval(long distance) {
        return distance <= FIRST_ADDITIONAL_INTERVAL;
    }

    private static boolean isFirstInterval(long distance) {
        return FIRST_ADDITIONAL_INTERVAL < distance && distance <= SECOND_ADDITIONAL_INTERVAL;
    }

    private static boolean isSecondInterval(long distance) {
        return SECOND_ADDITIONAL_INTERVAL < distance;
    }

    private static long calculateFirstIntervalFare(long distance) {
        return BASIC_CHARGE + calculateFareOverFirstDistance(distance);
    }

    private static long calculateSecondIntervalFare(long distance) {
        return BASIC_CHARGE + calculateFareOverFirstDistance(SECOND_ADDITIONAL_INTERVAL)
                + calculateFareOverSecondDistance(distance);
    }

    private static int calculateFareOverFirstDistance(long distance) {
        return (int) (Math.ceil((distance - FIRST_ADDITIONAL_INTERVAL) / FIRST_INTERVAL_UNIT) * OVER_FARE);
    }

    private static int calculateFareOverSecondDistance(long distance) {
        return (int) (Math.ceil((distance - SECOND_ADDITIONAL_INTERVAL) / SECOND_INTERVAL_UNIT) * OVER_FARE);
    }

    public static DistanceFarePolicy of(long distance) {
        return Arrays.stream(values())
                .filter(it -> it.distanceCondition.test(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리를 판단할 수 없습니다."));
    }

    public long calculate(long distance) {
        return this.distancePolicy.apply(distance);
    }

    static class Constants {
        private Constants() {
        }

        static final long BASIC_CHARGE = 1250;
        static final long OVER_FARE = 100;
        static final long FIRST_ADDITIONAL_INTERVAL = 10;
        static final long SECOND_ADDITIONAL_INTERVAL = 50;
        static final double FIRST_INTERVAL_UNIT = 5.0;
        static final double SECOND_INTERVAL_UNIT = 8.0;
    }
}
