package wooteco.subway.domain.distance;

import java.util.Arrays;
import java.util.function.Function;
import wooteco.subway.domain.Fare;
import wooteco.subway.exception.IllegalInputException;

public enum FareByDistancePolicy {

    BASE_DISTANCE_POLICY(DistanceType.BASE_DISTANCE, FareByDistancePolicy::calculateByBaseDistance),
    MIDDLE_DISTANCE_POLICY(DistanceType.MIDDLE_DISTANCE, FareByDistancePolicy::calculateByMiddleDistance),
    LONG_DISTANCE_POLICY(DistanceType.LONG_DISTANCE, FareByDistancePolicy::calculateByLongDistance);

    private static final int DISTANCE_OF_BASIC_FARE = 10;
    private static final int BASIC_FARE = 1250;
    private static final int DISTANCE_OF_OVER_FARE = 50;
    private static final int STANDARD_DISTANCE_OF_OVER_FARE = 5;
    private static final int MAX_STANDARD_DISTANCE_OF_OVER_FARE = 8;
    private static final int STANDARD_OF_OVER_FARE = 100;

    private final DistanceType distanceType;
    private final Function<Distance, Fare> calculatorFunction;

    FareByDistancePolicy(final DistanceType distanceType, final Function<Distance, Fare> calculatorFunction) {
        this.distanceType = distanceType;
        this.calculatorFunction = calculatorFunction;
    }

    public static Fare apply(final Distance distance) {
        return Arrays.stream(values())
                .filter(it -> it.distanceType.equals(DistanceType.from(distance)))
                .findFirst()
                .orElseThrow(() -> new IllegalInputException("해당하는 거리 타입을 찾을 수 없습니다."))
                .calculatorFunction.apply(distance);
    }

    private static Fare calculateByBaseDistance(final Distance distance) {
        return new Fare(BASIC_FARE);
    }

    private static Fare calculateByMiddleDistance(final Distance distance) {
        return new Fare(BASIC_FARE +
                calculateOverFare(distance.getValue() - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE));
    }

    private static Fare calculateByLongDistance(final Distance distance) {
        return new Fare(BASIC_FARE +
                calculateOverFare(DISTANCE_OF_OVER_FARE - DISTANCE_OF_BASIC_FARE, STANDARD_DISTANCE_OF_OVER_FARE) +
                calculateOverFare(distance.getValue() - DISTANCE_OF_OVER_FARE, MAX_STANDARD_DISTANCE_OF_OVER_FARE));
    }

    private static int calculateOverFare(final int value, final int standardValue) {
        return (int) ((Math.ceil((value - 1) / standardValue) + 1) * STANDARD_OF_OVER_FARE);
    }
}
