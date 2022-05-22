package wooteco.subway.domain.path;

import static wooteco.subway.domain.path.DiscountPolicy.Constants.*;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

enum DiscountPolicy {

    BABY_POLICY(age -> age < MIN_CHILD_AGE, fare -> 0),

    CHILD_POLICY(age -> age >= MIN_CHILD_AGE && age < MIN_TEENAGER_AGE,
        fare -> (int)((fare - DEFAULT_DEDUCTION) * CHILD_POLICY_RATE)),

    TEENAGER_POLICY(age -> age >= MIN_TEENAGER_AGE && age < MIN_ADULT_AGE,
        fare -> (int)((fare - DEFAULT_DEDUCTION) * TEENAGER_POLICY_RATE)),

    DEFAULT_POLICY(age -> age >= MIN_ADULT_AGE, fare -> fare);

    private final Predicate<Integer> ageCondition;
    private final Function<Integer, Integer> fareCalculation;

    DiscountPolicy(Predicate<Integer> ageCondition, Function<Integer, Integer> fareCalculation) {
        this.ageCondition = ageCondition;
        this.fareCalculation = fareCalculation;
    }

    static DiscountPolicy from(int age) {
        return Arrays.stream(values()).filter(it -> it.ageCondition.test(age)).findFirst().orElse(DEFAULT_POLICY);
    }

    int getFare(int fare) {
        return fareCalculation.apply(fare);
    }

    static class Constants {
        static final int DEFAULT_DEDUCTION = 350;

        static final int MIN_CHILD_AGE = 6;
        static final double CHILD_POLICY_RATE = 0.5;

        static final int MIN_TEENAGER_AGE = 13;
        static final double TEENAGER_POLICY_RATE = 0.8;

        static final int MIN_ADULT_AGE = 19;
    }
}
