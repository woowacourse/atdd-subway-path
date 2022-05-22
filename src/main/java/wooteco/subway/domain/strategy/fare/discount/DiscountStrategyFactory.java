package wooteco.subway.domain.strategy.fare.discount;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountStrategyFactory {
    CHILD(DiscountStrategyFactory::isChild, new ChildDiscountStrategy()),
    TEENAGER(DiscountStrategyFactory::isTeenager, new TeenagerDiscountStrategy()),
    OTHER(DiscountStrategyFactory::isOther, new OtherDiscountStrategy()),
    ;

    private static final int CHILD_AGE_MIN = 6;

    private static final int CHILD_AGE_MAX = 12;
    private static final int TEENAGER_AGE_MIN = 13;
    private static final int TEENAGER_AGE_MAX = 18;

    private final Predicate<Integer> predicate;
    private final DiscountStrategy discountStrategy;

    DiscountStrategyFactory(Predicate<Integer> predicate, DiscountStrategy discountStrategy) {
        this.predicate = predicate;
        this.discountStrategy = discountStrategy;
    }

    private static boolean isChild(int age) {
        return age >= CHILD_AGE_MIN && age <= CHILD_AGE_MAX;
    }

    private static boolean isTeenager(int age) {
        return age >= TEENAGER_AGE_MIN && age <= TEENAGER_AGE_MAX;
    }

    private static boolean isOther(int age) {
        return !(isChild(age) || isTeenager(age));
    }

    public static DiscountStrategy getDiscountStrategy(int age) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(age))
                .findFirst()
                .map(value -> value.discountStrategy)
                .orElseThrow(() -> new IllegalArgumentException("나이에 맞는 할인이 존재하지 않습니다"));
    }
}
