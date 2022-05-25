package wooteco.subway.domain.age;

import java.util.Arrays;
import wooteco.subway.domain.Fare;

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

    public Fare applyDiscount(final Fare fare) {
        return discountPolicy.apply(fare);
    }
}
