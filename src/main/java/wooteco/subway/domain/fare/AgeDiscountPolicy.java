package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscountPolicy {
    INFANT(age -> 1 <= age && age < 6, fare -> 0.0),
    CHILD(age -> 6 <= age && age < 13, AgeDiscountPolicy::discountChildFare),
    ADOLESCENT(age -> 13 <= age && age < 19, AgeDiscountPolicy::discountAdolescentFare),
    ADULT(age -> 19 <= age, fare -> fare);

    private static final int BASIC_DISCOUNT_CHARGE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double ADOLESCENT_DISCOUNT_RATE = 0.2;

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
}
