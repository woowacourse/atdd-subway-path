package wooteco.subway.domain.age;

import java.util.Arrays;

public enum FareByAgePolicy {

    BABY_POLICY(Age.BABY, new BabyDiscountPolicy()),
    KIDS_POLICY(Age.KIDS, new KidsDiscountPolicy()),
    TEENAGER_POLICY(Age.TEENAGER, new TeenagerDiscountPolicy()),
    ADULT_POLICY(Age.ADULT, new AdultDiscountPolicy());

    private final Age age;
    private final DiscountByAgePolicy discountPolicy;

    FareByAgePolicy(Age age, DiscountByAgePolicy discountPolicy) {
        this.age = age;
        this.discountPolicy = discountPolicy;
    }

    public static FareByAgePolicy find(final Age age) {
        return Arrays.stream(values())
                .filter(it -> it.age.equals(age))
                .findFirst()
                .orElseThrow();
    }

    public int applyDiscount(int fare) {
        return discountPolicy.apply(fare);
    }
}
