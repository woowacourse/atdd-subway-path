package wooteco.subway.domain.fare2;

import java.util.Arrays;

public enum AgeDiscountPolicy {

    BABY(0, Constants.CHILD_MIN_AGE, 0, 1.00),
    CHILD(Constants.CHILD_MIN_AGE, Constants.ADOLESCENT_MIN_AGE, 350, 0.50),
    ADOLESCENT(Constants.ADOLESCENT_MIN_AGE, Constants.ADULT_MIN_AGE, 350, 0.20),
    ADULT(Constants.ADULT_MIN_AGE, Constants.ELDERLY_MIN_AGE, 0, 0.00),
    ELDERLY(Constants.ELDERLY_MIN_AGE, 150, 0, 1.00),
    ;

    private static final String INVALID_AGE_RANGE_EXCEPTION = "0과 150 사이의 연령만 입력가능합니다.";

    private final int startInclusive;
    private final int endExclusive;
    private final int discountAmount;
    private final double discountRatio;

    AgeDiscountPolicy(int startInclusive, int endExclusive, int discountAmount, double discountRatio) {
        this.startInclusive = startInclusive;
        this.endExclusive = endExclusive;
        this.discountAmount = discountAmount;
        this.discountRatio = discountRatio;
    }

    public static AgeDiscountPolicy of(int value) {
        return Arrays.stream(values())
                .filter(age -> age.isAgeOf(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_AGE_RANGE_EXCEPTION));
    }

    private boolean isAgeOf(int value) {
        return value >= startInclusive && value < endExclusive;
    }

    public int applyDiscount(int fare) {
        return (int) ((fare - discountAmount) * (1 - discountRatio));
    }

    private static class Constants {

        static final int CHILD_MIN_AGE = 6;
        static final int ADOLESCENT_MIN_AGE = 13;
        static final int ADULT_MIN_AGE = 19;
        static final int ELDERLY_MIN_AGE = 65;
    }
}
