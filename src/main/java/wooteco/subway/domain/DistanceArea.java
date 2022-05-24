package wooteco.subway.domain;

import static wooteco.subway.domain.DistanceArea.Constant.*;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DistanceArea {

    FIRST_AREA(distance -> distance <= FIRST_RESTRICTION_DISTANCE, distance -> 0),
    SECOND_AREA(distance -> FIRST_RESTRICTION_DISTANCE < distance && distance <= SECOND_RESTRICTION_DISTANCE,
            DistanceArea::calculateOverTenDistance),
    THIRD_AREA(distance -> SECOND_RESTRICTION_DISTANCE < distance, DistanceArea::calculateOverFiftyDistance),
    ;

    private final Predicate<Integer> areaCalculator;
    private final UnaryOperator<Integer> additionalFareCalculator;

    DistanceArea(Predicate<Integer> areaCalculator, UnaryOperator<Integer> additionalFareCalculator) {
        this.areaCalculator = areaCalculator;
        this.additionalFareCalculator = additionalFareCalculator;
    }

    public static DistanceArea findDistance(int rawDistance) {
        return Arrays.stream(values())
                .filter(distanceArea -> distanceArea.areaCalculator.test(rawDistance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 거리입니다."));
    }

    public int calculateFare(int distance) {
        return additionalFareCalculator.apply(distance);
    }



    private static int calculateOverTenDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_RESTRICTION_DISTANCE) / FIRST_RESTRICTION_DIVIDE));
    }

    private static int calculateOverFiftyDistance(int distance) {
        return calculateOverTenDistance(SECOND_RESTRICTION_DISTANCE) + (int) (Math.ceil((distance - SECOND_RESTRICTION_DISTANCE) / SECOND_RESTRICTION_DIVIDE));
    }

    static class Constant {

        static final int FIRST_RESTRICTION_DISTANCE = 10;
        static final int SECOND_RESTRICTION_DISTANCE = 50;
        static final double FIRST_RESTRICTION_DIVIDE = 5.0;
        static final double SECOND_RESTRICTION_DIVIDE = 8.0;
    }
}
