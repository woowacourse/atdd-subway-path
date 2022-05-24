package wooteco.subway.domain.fare;

import java.util.Arrays;

public final class AgeDiscountFare extends FarePolicy {

    private static final int BASIC_DISCOUNT_AMOUNT = 350;
    private static final int CHILD_MIN_AGE = 6;
    private static final int ADOLESCENT_MIN_AGE = 13;
    private static final int ADULT_MIN_AGE = 19;
    private static final int ELDERLY_MIN_AGE = 65;

    private final Age age;

    public AgeDiscountFare(Fare delegate, int age) {
        super(delegate);
        this.age = Age.of(age);
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        return age.applyDiscount(fare);
    }

    private enum Age {

        BABY(0, CHILD_MIN_AGE, 0, 1.00),
        CHILD(CHILD_MIN_AGE, ADOLESCENT_MIN_AGE, BASIC_DISCOUNT_AMOUNT, 0.50),
        ADOLESCENT(ADOLESCENT_MIN_AGE, ADULT_MIN_AGE, BASIC_DISCOUNT_AMOUNT, 0.20),
        ADULT(ADULT_MIN_AGE, ELDERLY_MIN_AGE, 0, 0.00),
        ELDERLY(ELDERLY_MIN_AGE, 150, 0, 1.00),
        ;

        static final String INVALID_AGE_RANGE_EXCEPTION = "0과 150 사이의 연령만 입력가능합니다.";

        final int startInclusive;
        final int endExclusive;
        final int discountAmount;
        final double discountRatio;

        Age(int startInclusive, int endExclusive, int discountAmount, double discountRatio) {
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
            this.discountAmount = discountAmount;
            this.discountRatio = discountRatio;
        }

        static Age of(int value) {
            return Arrays.stream(values())
                    .filter(age -> age.isAgeOf(value))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(INVALID_AGE_RANGE_EXCEPTION));
        }

        boolean isAgeOf(int value) {
            return value >= startInclusive && value < endExclusive;
        }

        int applyDiscount(int fare) {
            return (int) ((fare - discountAmount) * (1 - discountRatio));
        }
    }
}
