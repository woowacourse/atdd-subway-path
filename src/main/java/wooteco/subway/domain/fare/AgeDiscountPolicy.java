package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscountPolicy {
    INFANT(age -> 1 <= age && age < 6, fare -> 0.0),
    CHILD(age -> 6 <= age && age < 13, fare -> (fare - 350) * 0.5),
    ADOLESCENT(age -> 13 <= age && age < 19, fare -> (fare - 350) * 0.8),
    ADULT(age -> 19 <= age, fare -> fare);

    private final Predicate<Integer> ageCondition;
    private final UnaryOperator<Double> discountPolicy;

    AgeDiscountPolicy(Predicate<Integer> ageCondition, UnaryOperator<Double> discountPolicy) {
        this.ageCondition = ageCondition;
        this.discountPolicy = discountPolicy;
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
