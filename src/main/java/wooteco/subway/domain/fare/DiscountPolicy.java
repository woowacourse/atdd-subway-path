package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {

    INFANT(isEvenOrLessThanFive(), 0L, 0),
    CHILDREN(isBetweenSixAndTwelve(), 350L, 0.5),
    TEENAGER(isBetweenThirteenAndEightTeen(), 350L, 0.8),
    ADULT(isBetweenNineTeenAndSixtyFour(), 0L, 1),
    SENIOR(isEvenOrGreaterThanSixtyFive(), 0L, 0),
    ;

    private Predicate<Long> discountSectionFinder;
    private Long discountValue;
    private double discountRate;

    DiscountPolicy(Predicate<Long> discountSectionFinder, Long discountValue, double discountRate) {
        this.discountSectionFinder = discountSectionFinder;
        this.discountValue = discountValue;
        this.discountRate = discountRate;
    }

    public static long calculateFareByAge(long fare, long age) {
        final DiscountPolicy discountStrategy = Arrays.stream(values())
                .filter(discountSection -> discountSection.discountSectionFinder.test(age))
                .findAny()
                .orElse(ADULT);

        return Double.valueOf(
                        (fare - discountStrategy.discountValue) * discountStrategy.discountRate)
                .longValue();
    }

    private static Predicate<Long> isEvenOrLessThanFive() {
        return age -> age < 6;
    }

    private static Predicate<Long> isBetweenSixAndTwelve() {
        return age -> 6 <= age && age <= 12;
    }

    private static Predicate<Long> isBetweenThirteenAndEightTeen() {
        return age -> 13 <= age && age <= 18;
    }

    private static Predicate<Long> isBetweenNineTeenAndSixtyFour() {
        return age -> 19 <= age && age <= 64;
    }

    private static Predicate<Long> isEvenOrGreaterThanSixtyFive() {
        return age -> 65 <= age;
    }
}
