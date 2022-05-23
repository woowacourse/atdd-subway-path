package wooteco.subway.domain.constant;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public enum AgeFarePolicy {

    CHILD_AGE_POLICY(AgeFarePolicy::childCondition, AgeFarePolicy::calculateChildPolicy),
    YOUTH_AGE_POLICY(AgeFarePolicy::youthCondition, AgeFarePolicy::calculateYouthPolicy),
    ADULT_AGE_POLICY(AgeFarePolicy::adultCondition, AgeFarePolicy::calculateAdultPolicy);

    private final IntPredicate condition;
    private final IntUnaryOperator calculator;

    AgeFarePolicy(IntPredicate condition, IntUnaryOperator calculator) {
        this.condition = condition;
        this.calculator = calculator;
    }

    private static boolean childCondition(int age) {
        return age >= 6 && age < 13;
    }

    private static boolean youthCondition(int age) {
        return age >= 13 && age < 19;
    }

    private static boolean adultCondition(int age) {
        return age >= 19 || (age >= 0 && age < 6);
    }

    private static int calculateChildPolicy(int amount) {
        return (int)((amount -350) * 0.5);
    }

    private static int calculateYouthPolicy(int amount) {
        return (int)((amount -350) * 0.8);
    }

    private static int calculateAdultPolicy(int amount) {
        return amount;
    }

    public IntPredicate condition() {
        return condition;
    }

    public IntUnaryOperator calculator() {
        return calculator;
    }
}
