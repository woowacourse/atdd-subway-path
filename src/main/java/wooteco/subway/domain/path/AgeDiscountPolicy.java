package wooteco.subway.domain.path;

import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.*;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

enum AgeDiscountPolicy {

    BABY_POLICY(age -> age < MIN_CHILD_AGE, fare -> 0),

    CHILD_POLICY(age -> age >= MIN_CHILD_AGE && age < MIN_TEENAGER_AGE,
        fare -> calculateDiscountedFare(fare, CHILD_DISCOUNT_RATE)),

    TEENAGER_POLICY(age -> age >= MIN_TEENAGER_AGE && age < MIN_ADULT_AGE,
        fare -> calculateDiscountedFare(fare, TEENAGER_DISCOUNT_RATE)),

    DEFAULT_POLICY(age -> age >= MIN_ADULT_AGE, fare -> fare);

    private final Predicate<Integer> ageCondition;
    private final Function<Integer, Integer> fareCalculation;

    AgeDiscountPolicy(Predicate<Integer> ageCondition, Function<Integer, Integer> fareCalculation) {
        this.ageCondition = ageCondition;
        this.fareCalculation = fareCalculation;
    }

    private static int calculateDiscountedFare(Integer fare, double discountRate) {
        return (int)((fare - DEFAULT_DEDUCTION) * (1 - discountRate));
    }

    static AgeDiscountPolicy from(int age) {
        return Arrays.stream(values())
            .filter(it -> it.ageCondition.test(age))
            .findFirst()
            .orElse(DEFAULT_POLICY);
    }

    int getFare(int fare) {
        return fareCalculation.apply(fare);
    }

    static class Constants {

        static final int DEFAULT_DEDUCTION = 350;

        static final int MIN_CHILD_AGE = 6;
        static final double CHILD_DISCOUNT_RATE = 0.5;

        static final int MIN_TEENAGER_AGE = 13;
        static final double TEENAGER_DISCOUNT_RATE = 0.2;

        static final int MIN_ADULT_AGE = 19;
    }
}
