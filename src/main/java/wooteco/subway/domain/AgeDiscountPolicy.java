package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {
    BABY(age -> age >= 0 && age <= 5, discountFare -> 0),
    CHILDREN(age -> age >= 6 && age <= 12, discountFare -> (int) ((discountFare - 350) * 0.5)),
    TEENAGER(age -> age >= 13 && age <= 18, discountFare -> (int) ((discountFare - 350) * 0.2)),
    ADULT(age -> age >= 19, discountFare -> 0);

    private final Predicate<Integer> agePredicates;
    private final Function<Integer, Integer> discountPolicy;

    AgeDiscountPolicy(Predicate<Integer> agePredicates, Function<Integer, Integer> discountPolicy) {
        this.agePredicates = agePredicates;
        this.discountPolicy = discountPolicy;
    }

    public static AgeDiscountPolicy from(int age) {
        return Arrays.stream(values())
                .filter(it -> it.agePredicates.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("올바른 나이가 아닙니다."));
    }

    public int discountFare(int originFare) {
        return discountPolicy.apply(originFare);
    }
}
