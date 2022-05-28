package wooteco.subway.domain.path;

import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.ADOLESCENT_DISCOUNT_RATE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.ADOLESCENT_MIN_AGE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.ADULT_MIN_AGE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.BASIC_DISCOUNT_CHARGE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.CHILD_DISCOUNT_RATE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.CHILD_MIN_AGE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.INFANT_MIN_AGE;
import static wooteco.subway.domain.path.AgeDiscountPolicy.Constants.NO_CHARGE;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscountPolicy {
    INFANT(age -> INFANT_MIN_AGE <= age && age < CHILD_MIN_AGE, fare -> NO_CHARGE),
    CHILD(age -> CHILD_MIN_AGE <= age && age < ADOLESCENT_MIN_AGE, AgeDiscountPolicy::discountChildFare),
    ADOLESCENT(age -> ADOLESCENT_MIN_AGE <= age && age < ADULT_MIN_AGE, AgeDiscountPolicy::discountAdolescentFare),
    ADULT(age -> ADULT_MIN_AGE <= age, fare -> fare);

    private final Predicate<Integer> ageCondition;
    private final UnaryOperator<Double> discountPolicy;

    AgeDiscountPolicy(Predicate<Integer> ageCondition, UnaryOperator<Double> discountPolicy) {
        this.ageCondition = ageCondition;
        this.discountPolicy = discountPolicy;
    }

    private static double discountChildFare(Double fare) {
        return (fare - BASIC_DISCOUNT_CHARGE) * (1 - CHILD_DISCOUNT_RATE);
    }

    private static double discountAdolescentFare(Double fare) {
        return (fare - BASIC_DISCOUNT_CHARGE) * (1 - ADOLESCENT_DISCOUNT_RATE);
    }

    public static AgeDiscountPolicy of(int age) {
        return Arrays.stream(values())
                .filter(it -> it.ageCondition.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이는 1살 이상이어야합니다."));
    }

    public int apply(double fare) {
        return this.discountPolicy.apply(fare).intValue();
    }

    static class Constants {
        private Constants() {
        }

        static final int INFANT_MIN_AGE = 1;
        static final int CHILD_MIN_AGE = 6;
        static final int ADOLESCENT_MIN_AGE = 13;
        static final int ADULT_MIN_AGE = 19;

        static final double NO_CHARGE = 0.0;
        static final int BASIC_DISCOUNT_CHARGE = 350;
        static final double CHILD_DISCOUNT_RATE = 0.5;
        static final double ADOLESCENT_DISCOUNT_RATE = 0.2;
    }
}
