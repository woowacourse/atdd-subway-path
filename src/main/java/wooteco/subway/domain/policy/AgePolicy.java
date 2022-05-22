package wooteco.subway.domain.policy;

import wooteco.subway.domain.fare.FarePolicy;

public class AgePolicy implements FarePolicy {
    private static final int DEDUCTION_FARE = 350;
    private static final int BABY_MAX_AGE = 6;
    private static final int CHILD_MAX_AGE = 13;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final int TEENAGER_MAX_AGE = 19;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;
    private final int age;

    public AgePolicy(int age) {
        this.age = age;
    }

    @Override
    public double calculate(double baseFare) {
        if (age < BABY_MAX_AGE) {
            return 0;
        }
        if (age < CHILD_MAX_AGE) {
            return (baseFare - DEDUCTION_FARE) * CHILD_DISCOUNT_RATE;
        }
        if (age < TEENAGER_MAX_AGE) {
            return (baseFare - DEDUCTION_FARE) * TEENAGER_DISCOUNT_RATE;
        }
        return baseFare;
    }
}
