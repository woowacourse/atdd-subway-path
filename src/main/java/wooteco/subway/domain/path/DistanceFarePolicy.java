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

    private final Predicate<Integer> distanceCondition;

    private final UnaryOperator<Integer> distancePolicy;

    DistanceFarePolicy(Predicate<Integer> distanceCondition, UnaryOperator<Integer> distancePolicy) {
        this.distanceCondition = distanceCondition;
        this.distancePolicy = distancePolicy;
    }

    private static boolean isDefaultInterval(Integer distance) {
        return distance <= FIRST_ADDITIONAL_INTERVAL;
    }

    private static boolean isFirstInterval(Integer distance) {
        return FIRST_ADDITIONAL_INTERVAL < distance && distance <= SECOND_ADDITIONAL_INTERVAL;
    }

    private static boolean isSecondInterval(Integer distance) {
        return SECOND_ADDITIONAL_INTERVAL < distance;
    }

    private static int calculateFirstIntervalFare(int distance) {
        return BASIC_CHARGE + calculateFareOverFirstDistance(distance);
    }

    private static int calculateSecondIntervalFare(Integer distance) {
        return BASIC_CHARGE + calculateFareOverFirstDistance(SECOND_ADDITIONAL_INTERVAL)
                + calculateFareOverSecondDistance(distance);
    }

    private static int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_ADDITIONAL_INTERVAL) / FIRST_INTERVAL_UNIT) * OVER_FARE);
    }

    private static int calculateFareOverSecondDistance(int distance) {
        return (int) (Math.ceil((distance - SECOND_ADDITIONAL_INTERVAL) / SECOND_INTERVAL_UNIT) * OVER_FARE);
    }

    public static DistanceFarePolicy of(long distance) {
        return Arrays.stream(values())
                .filter(it -> it.distanceCondition.test((int) distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리를 판단할 수 없습니다."));
    }

    public int calculate(long distance) {
        return this.distancePolicy.apply((int) distance);
    }

    static class Constants {
        private Constants() {
        }

        static final int BASIC_CHARGE = 1250;
        static final int OVER_FARE = 100;
        static final int FIRST_ADDITIONAL_INTERVAL = 10;
        static final int SECOND_ADDITIONAL_INTERVAL = 50;
        static final double FIRST_INTERVAL_UNIT = 5.0;
        static final double SECOND_INTERVAL_UNIT = 8.0;
    }
}
