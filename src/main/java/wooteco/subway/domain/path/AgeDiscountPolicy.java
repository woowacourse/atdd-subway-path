package wooteco.subway.domain.path;

import java.util.Arrays;

import wooteco.subway.domain.Age;

public enum AgeDiscountPolicy {

    PRESCHOOLER(0, 6, 0, 100),
    SCHOOL_AGED_CHILD(6, 13, 350, 50),
    ADOLESCENT(13, 19, 350, 20);

    private final long lowerLimit;
    private final long upperLimit;
    private final long deductible;
    private final long discountPercent;

    AgeDiscountPolicy(long lowerLimit, long upperLimit, long deductible, long discountPercent) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.deductible = deductible;
        this.discountPercent = discountPercent;
    }

    public static long calculateByPolicy(long fare, Age age) {
        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(policy -> policy.isSatisfied(age))
                .map(policy -> policy.calculate(fare))
                .findAny()
                .orElse(fare);
    }

    private boolean isSatisfied(Age age) {
        return age.isSameOrHigherThan(lowerLimit) && age.isLowerThan(upperLimit);
    }

    private long calculate(long fare) {
        return (fare - deductible) * (100 - discountPercent) / 100;
    }
}
