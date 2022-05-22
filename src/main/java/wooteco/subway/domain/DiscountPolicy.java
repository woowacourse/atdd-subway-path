package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import wooteco.subway.exception.InvalidAgeException;

public enum DiscountPolicy {
    CHILDREN(DiscountPolicy::isChildrenAge, DiscountPolicy::applyDiscountChildren),
    TEENAGER(DiscountPolicy::isTeenagerAge, DiscountPolicy::applyDiscountTeenager),
    OTHER(age -> !isChildrenAge(age) && !isTeenagerAge(age), money -> money)
    ;

    private static final int DEDUCTIBLE = 350;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;

    private final Predicate<Integer> agePredicate;
    private final IntUnaryOperator discountOperator;

    DiscountPolicy(final Predicate<Integer> agePredicate, final IntUnaryOperator discountOperator) {
        this.agePredicate = agePredicate;
        this.discountOperator = discountOperator;
    }

    public static DiscountPolicy createByAge(final int age) {
        return Arrays.stream(values())
                .filter(discountPolicy -> discountPolicy.agePredicate.test(age))
                .findAny()
                .orElseThrow(InvalidAgeException::new);
    }

    private static boolean isChildrenAge(final int age) {
        return age >= 6 && age < 13;
    }

    private static int applyDiscountChildren(final int money) {
        final int discountedMoney = (int)(((money - DEDUCTIBLE) * CHILDREN_DISCOUNT_RATE));
        return money - discountedMoney;
    }

    private static boolean isTeenagerAge(final int age) {
        return age >= 13 && age < 19;
    }

    private static int applyDiscountTeenager(final int money) {
        final int discountedMoney = (int)(((money - DEDUCTIBLE) * TEENAGER_DISCOUNT_RATE));
        return money - discountedMoney;
    }

    public int applyDiscount(final int money) {
        return discountOperator.applyAsInt(money);
    }
}
