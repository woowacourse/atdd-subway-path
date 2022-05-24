package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DiscountPolicy {
    BABY_OR_SENIOR(Age::isBabyOrSenior, DiscountPolicy::discountForBabyOrSenior),
    CHILDREN(Age::isChildren, DiscountPolicy::discountForChildren),
    TEENAGER(Age::isTeenager, DiscountPolicy::discountForTeenager),
    ADULT(Age::isAdult, DiscountPolicy::discountForAdult),
    ;

    private static final int DEDUCTING_AMOUNT = 350;

    private final Predicate<Age> agePredicate;
    private final UnaryOperator<Fare> discount;

    DiscountPolicy(Predicate<Age> agePredicate, UnaryOperator<Fare> discount) {
        this.agePredicate = agePredicate;
        this.discount = discount;
    }

    private static Fare discountForBabyOrSenior(Fare fare) {
        return new Fare(0);
    }

    private static Fare discountForChildren(Fare fare) {
        return fare.subtract(DiscountPolicy.DEDUCTING_AMOUNT).multiply(0.5);
    }

    private static Fare discountForTeenager(Fare fare) {
        return fare.subtract(DiscountPolicy.DEDUCTING_AMOUNT).multiply(0.8);
    }

    private static Fare discountForAdult(Fare fare) {
        return fare;
    }

    public static DiscountPolicy from(Age age) {
        return Arrays.stream(values())
                .filter(strategy -> strategy.agePredicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("입력된 연령으로 연령 할인 정책을 가져올 수 없습니다."));
    }

    public Fare discount(Fare original) {
        return discount.apply(original);
    }
}
