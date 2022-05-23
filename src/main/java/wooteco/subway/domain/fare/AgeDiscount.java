package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscount {
    BABY(age -> age < Constants.MAX_BABY_AGE, fare -> Constants.FREE_FARE),
    CHILDREN(age -> age < Constants.MAX_CHILDREN_AGE,
            fare -> calculateDiscountedFare(fare, Constants.CHILDREN_FARE_RATE)),
    TEENAGER(age -> age < Constants.MAX_TEENAGER_AGE,
            fare -> calculateDiscountedFare(fare, Constants.TEENAGER_FARE_RATE)),
    ADULT(age -> age >= Constants.MAX_TEENAGER_AGE, fare -> fare),
    ;

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private static class Constants {
        public static final int MAX_BABY_AGE = 6;
        public static final int MAX_CHILDREN_AGE = 13;
        public static final int MAX_TEENAGER_AGE = 19;

        public static final int FREE_FARE = 0;
        public static final double CHILDREN_FARE_RATE = 0.5;
        public static final double TEENAGER_FARE_RATE = 0.8;
    }

    private final Predicate<Integer> agePredicate;
    private final UnaryOperator<Integer> discountOperator;

    AgeDiscount(Predicate<Integer> agePredicate, UnaryOperator<Integer> discountOperator) {
        this.agePredicate = agePredicate;
        this.discountOperator = discountOperator;
    }

    public int getDiscountedFare(final int fare) {
        return discountOperator.apply(fare);
    }

    private static int calculateDiscountedFare(final int fare, final double ratio) {
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * ratio);
    }

    public static AgeDiscount findAgeDiscount(final int age) {
        return Arrays.stream(AgeDiscount.values())
                .filter(discount -> discount.agePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("입력한 나이에 해당하는 할인 정책이 존재하지 않습니다."));
    }
}
