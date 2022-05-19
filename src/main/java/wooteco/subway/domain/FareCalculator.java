package wooteco.subway.domain;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

@Component
public class FareCalculator {

    private static final int BASIS_FARE = 1_250;
    private static final int BASIC_FARE_OVER_50KM = 2_050;
    private static final int FIRST_FARE_INCREASE_STANDARD = 10;
    private static final int LAST_FARE_INCREASE_STANDARD = 50;
    private static final int FIRST_FARE_INCREASE_STANDARD_UNIT = 5;
    private static final int LAST_FARE_INCREASE_STANDARD_UNIT = 8;
    private static final int INCREASE_RATE = 100;
    public int calculateFare(int distance) {
        return findFareByDistance(distance);
    }

    private Integer findFareByDistance(int distance) {
        Integer fare = Arrays.stream(DistancePolicy.values())
                .filter(it -> it.condition.test(distance))
                .map(it -> it.calculator.applyAsInt(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
        return fare;
    }

    private enum DistancePolicy {
        SHORT_DISTANCE_POLICY(DistancePolicy::shortDistanceCondition, DistancePolicy::calculateShortPolicy),
        MIDDLE_DISTANCE_POLICY(DistancePolicy::middleDistanceCondition, DistancePolicy::calculateMiddlePolicy),
        LONG_DISTANCE_POLICY(DistancePolicy::longDistanceCondition, DistancePolicy::calculateLongPolicy);

        private final Predicate<Integer> condition;
        private final IntUnaryOperator calculator;

        DistancePolicy(Predicate<Integer> condition, IntUnaryOperator calculator) {
            this.condition = condition;
            this.calculator = calculator;
        }

        private static boolean shortDistanceCondition(int distance) {
            return distance >= 0 && distance <= FIRST_FARE_INCREASE_STANDARD;
        }

        private static boolean middleDistanceCondition(int distance) {
            return distance > 0 && distance <= LAST_FARE_INCREASE_STANDARD;
        }

        private static boolean longDistanceCondition(int distance) {
            return distance > FIRST_FARE_INCREASE_STANDARD;
        }

        private static int calculateShortPolicy(int distance) {
            return BASIS_FARE;
        }

        private static int calculateMiddlePolicy(int distance) {
            return BASIS_FARE + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - FIRST_FARE_INCREASE_STANDARD) / FIRST_FARE_INCREASE_STANDARD_UNIT);
        }

        private static int calculateLongPolicy(int distance) {
            return BASIC_FARE_OVER_50KM + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - LAST_FARE_INCREASE_STANDARD) / LAST_FARE_INCREASE_STANDARD_UNIT);
        }
    }
}
