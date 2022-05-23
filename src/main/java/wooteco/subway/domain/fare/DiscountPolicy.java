package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    FREE((age) -> age.isLessThanOrEqualTo(5) || age.isGreaterThanOrEqualTo(65), 1),
    CHILDREN((age) -> age.isBetween(6, 12), 0.5),
    TEENAGER((age) -> age.isBetween(13, 18), 0.2),
    ADULT((age) -> age.isGreaterThanOrEqualTo(19), 0),
    ;

    private static final int DEDUCTING_AMOUNT = 350;

    private final Predicate<Age> agePredicate;
    private final double discountRate;

    DiscountPolicy(Predicate<Age> agePredicate, double discountRate) {
        this.agePredicate = agePredicate;
        this.discountRate = discountRate;
    }

    public static DiscountPolicy from(Age age) {
        return Arrays.stream(values())
                .filter(strategy -> strategy.agePredicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("입력된 연령으로 연령 할인 정책을 가져올 수 없습니다."));
    }

    public Fare discount(Fare original) {
        if (discountRate == 0) {
            return original;
        }

        Fare deducted = original.subtract(DEDUCTING_AMOUNT);
        return deducted.multiply(1 - discountRate);
    }
}
