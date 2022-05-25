package wooteco.subway.domain.fare;

import static wooteco.subway.domain.fare.DiscountPolicy.Constants.ADULT_DISCOUNT_AMOUNT;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.CHILDREN_DISCOUNT_AMOUNT;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.CHILDREN_DISCOUNT_RATE;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.FREE_RIDE_RATE;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.INFANT_DISCOUNT_AMOUNT;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.NO_DISCOUNT_RATE;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.SENIOR_DISCOUNT_AMOUNT;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.TEENAGER_DISCOUNT_AMOUNT;
import static wooteco.subway.domain.fare.DiscountPolicy.Constants.TEENAGER_DISCOUNT_RATE;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {

    INFANT(isLessThanSix(), INFANT_DISCOUNT_AMOUNT, FREE_RIDE_RATE),
    CHILDREN(isBetweenSixAndTwelve(), CHILDREN_DISCOUNT_AMOUNT, CHILDREN_DISCOUNT_RATE),
    TEENAGER(isBetweenThirteenAndEightTeen(), TEENAGER_DISCOUNT_AMOUNT, TEENAGER_DISCOUNT_RATE),
    ADULT(isBetweenNineTeenAndSixtyFour(), ADULT_DISCOUNT_AMOUNT, NO_DISCOUNT_RATE),
    SENIOR(isEvenOrGreaterThanSixtyFive(), SENIOR_DISCOUNT_AMOUNT, FREE_RIDE_RATE),
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

    private static Predicate<Long> isLessThanSix() {
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

    static class Constants {
        static final long INFANT_DISCOUNT_AMOUNT = 0L;
        static final long CHILDREN_DISCOUNT_AMOUNT = 350L;
        static final long TEENAGER_DISCOUNT_AMOUNT = 350L;
        static final long ADULT_DISCOUNT_AMOUNT = 0L;
        static final long SENIOR_DISCOUNT_AMOUNT = 0L;

        static final double FREE_RIDE_RATE = 0.0;
        static final double CHILDREN_DISCOUNT_RATE = 0.5;
        static final double TEENAGER_DISCOUNT_RATE = 0.8;
        static final double NO_DISCOUNT_RATE = 1.0;
    }
}
