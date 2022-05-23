package wooteco.subway.domain.constant;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum DistanceFarePolicy {

    SHORT_DISTANCE_POLICY(DistanceFarePolicy::shortDistanceCondition, DistanceFarePolicy::calculateShortPolicy),
    MIDDLE_DISTANCE_POLICY(DistanceFarePolicy::middleDistanceCondition, DistanceFarePolicy::calculateMiddlePolicy),
    LONG_DISTANCE_POLICY(DistanceFarePolicy::longDistanceCondition, DistanceFarePolicy::calculateLongPolicy);

    private static final int BASIS_FARE = 1_250;
    private static final int FARE_AT_50KM = 2_050;
    private static final int FIRST_FARE_INCREASE_STANDARD = 10;
    private static final int LAST_FARE_INCREASE_STANDARD = 50;
    private static final int FIRST_FARE_INCREASE_STANDARD_UNIT = 5;
    private static final int LAST_FARE_INCREASE_STANDARD_UNIT = 8;
    private static final int INCREASE_RATE = 100;

    private final IntPredicate condition;
    private final IntUnaryOperator calculator;

    DistanceFarePolicy(IntPredicate condition, IntUnaryOperator calculator) {
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
        return FARE_AT_50KM + INCREASE_RATE *
                (int) Math.ceil((double) (distance - LAST_FARE_INCREASE_STANDARD) / LAST_FARE_INCREASE_STANDARD_UNIT);
    }

    public IntPredicate condition() {
        return condition;
    }

    public IntUnaryOperator calculator() {
        return calculator;
    }
}
