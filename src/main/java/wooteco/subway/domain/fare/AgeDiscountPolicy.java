package wooteco.subway.domain.fare;

import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.CHILD_FARE_PERCENT;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.CHILD_MAX_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.CHILD_MIN_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.DEFAULT_DISCOUNT_FARE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.GENERAL_MAX_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.GENERAL_MIN_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.MAX_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.MIN_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.SENIOR_MIN_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.TEEN_FARE_PERCENT;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.TEEN_MAX_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.TEEN_MIN_AGE;
import static wooteco.subway.domain.fare.AgeDiscountPolicy.Constants.TODDLER_MAX_AGE;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscountPolicy {

    GENERAL(
            age -> age >= GENERAL_MIN_AGE && age <= GENERAL_MAX_AGE,
            fare -> fare
    ),
    TEEN(
            age -> age >= TEEN_MIN_AGE && age <= TEEN_MAX_AGE,
            fare -> (int) ((fare - DEFAULT_DISCOUNT_FARE) * TEEN_FARE_PERCENT)
    ),
    CHILD(
            age -> age >= CHILD_MIN_AGE && age <= CHILD_MAX_AGE,
            fare -> (int) ((fare - DEFAULT_DISCOUNT_FARE) * CHILD_FARE_PERCENT)
    ),
    SENIOR(
            age -> age >= SENIOR_MIN_AGE,
            fare -> 0
    ),
    TODDLER(
            age -> age <= TODDLER_MAX_AGE,
            fare -> 0
    );


    private final Predicate<Integer> ageRange;
    private final Function<Integer, Integer> discountCondition;

    AgeDiscountPolicy(Predicate<Integer> ageRange, Function<Integer, Integer> discountCondition) {
        this.ageRange = ageRange;
        this.discountCondition = discountCondition;
    }

    public static AgeDiscountPolicy from(final int age) {
        validateAgeRange(age);
        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(it -> it.ageRange.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 나이의 할인 정책을 찾을 수 없습니다."));
    }

    private static void validateAgeRange(final int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException("나이를 다시 확인하고 입력해주세요.");
        }
    }

    public static int discount(final int fare, final AgeDiscountPolicy discountPolicy) {
        if (discountPolicy == TEEN || discountPolicy == CHILD) {
            validateOverDefaultDiscountFare(fare);
        }
        return discountPolicy.discountCondition.apply(fare);
    }

    private static void validateOverDefaultDiscountFare(final int fare) {
        if (fare < DEFAULT_DISCOUNT_FARE) {
            throw new IllegalArgumentException("할인 가능한 요금보다 적습니다.");
        }
    }

    protected static class Constants {

        public static final int DEFAULT_DISCOUNT_FARE = 350;
        public static final int MIN_AGE = 1;
        public static final int MAX_AGE = 100;
        public static final int GENERAL_MIN_AGE = 19;
        public static final int GENERAL_MAX_AGE = 64;
        public static final int TEEN_MIN_AGE = 13;
        public static final int TEEN_MAX_AGE = 18;
        public static final int CHILD_MIN_AGE = 6;
        public static final int CHILD_MAX_AGE = 12;
        public static final int SENIOR_MIN_AGE = 65;
        public static final int TODDLER_MAX_AGE = 5;
        public static final double TEEN_FARE_PERCENT = 0.8;
        public static final double CHILD_FARE_PERCENT = 0.5;
    }
}
