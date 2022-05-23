package wooteco.subway.domain.strategy.discount;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountStrategyMapper {

    DEFAULT_STRATEGY(it -> it < 6 || it >= 19, DefaultDiscountStrategy.getInstance()),
    CHILD_STRATEGY(it -> 6 <= it && it < 13, ChildDiscountStrategy.getInstance()),
    TEENAGER_STRATEGY(it -> 13 <= it && it < 19, TeenagerDiscountStrategy.getInstance()),
    ;

    private final Predicate<Integer> predicate;
    private final DiscountStrategy strategy;

    DiscountStrategyMapper(final Predicate<Integer> predicate,
                           final DiscountStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    public static DiscountStrategy findStrategyBy(final int age) {
        return Arrays.stream(values())
                .filter(it -> it.predicate.test(age))
                .findFirst()
                .orElseThrow()
                .strategy;
    }
}
