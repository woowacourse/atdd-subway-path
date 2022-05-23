package wooteco.subway.domain;

import static wooteco.subway.domain.DiscountPolicy.Constants.CHILDREN_DISCOUNT_PERCENTAGE;
import static wooteco.subway.domain.DiscountPolicy.Constants.DEDUCTION_AMOUNT;
import static wooteco.subway.domain.DiscountPolicy.Constants.FREE_FARE;
import static wooteco.subway.domain.DiscountPolicy.Constants.TEENAGER_DISCOUNT_PERCENTAGE;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public enum DiscountPolicy {

    BABY(
        age -> age < 6,
        value -> FREE_FARE),
    CHILDREN(
        age -> age >= 6 && age < 13,
        value -> (int) (value - ((value - DEDUCTION_AMOUNT) * TEENAGER_DISCOUNT_PERCENTAGE))),
    TEENAGER(
        age -> age >= 13 && age < 19,
        value -> (int) (value - ((value - DEDUCTION_AMOUNT) * CHILDREN_DISCOUNT_PERCENTAGE))),
    NO_DISCOUNT_AGE_GROUP(
        age -> false,
        value -> value);

    private final IntPredicate agePolicy;
    private final IntUnaryOperator fareDiscountPolicy;

    DiscountPolicy(IntPredicate agePolicy, IntUnaryOperator fareDiscountPolicy) {
        this.agePolicy = agePolicy;
        this.fareDiscountPolicy = fareDiscountPolicy;
    }

    public static int getDiscountValue(int fare, int age) {
        return findByAge(age).getFareDiscountPolicy().applyAsInt(fare);
    }

    private static DiscountPolicy findByAge(int age) {
        return Arrays.stream(DiscountPolicy.values())
            .filter(ageGroup -> ageGroup.getAgePolicy().test(age))
            .findFirst()
            .orElse(NO_DISCOUNT_AGE_GROUP);
    }

    public IntPredicate getAgePolicy() {
        return agePolicy;
    }

    private IntUnaryOperator getFareDiscountPolicy() {
        return fareDiscountPolicy;
    }

    public static class Constants {

        public static final int FREE_FARE = 0;
        public static final int DEDUCTION_AMOUNT = 350;
        public static final double CHILDREN_DISCOUNT_PERCENTAGE = 0.2;
        public static final double TEENAGER_DISCOUNT_PERCENTAGE = 0.5;
    }
}
