package wooteco.subway.domain.age;

import java.util.Arrays;

public enum FareByAgePolicy {

    BABY_POLICY(AgeType.BABY, new BabyDiscountPolicy()),
    KIDS_POLICY(AgeType.KIDS, new KidsDiscountPolicy()),
    TEENAGER_POLICY(AgeType.TEENAGER, new TeenagerDiscountPolicy()),
    ADULT_POLICY(AgeType.ADULT, new AdultDiscountPolicy());

    private final AgeType ageType;
    private final DiscountByAgePolicy discountPolicy;

    FareByAgePolicy(final AgeType ageType, final DiscountByAgePolicy discountPolicy) {
        this.ageType = ageType;
        this.discountPolicy = discountPolicy;
    }

    public static FareByAgePolicy from(final AgeType ageType) {
        return Arrays.stream(values())
                .filter(it -> it.ageType.equals(ageType))
                .findFirst()
                .orElseThrow();
    }

    public int applyDiscount(final int fare) {
        return discountPolicy.apply(fare);
    }
}
