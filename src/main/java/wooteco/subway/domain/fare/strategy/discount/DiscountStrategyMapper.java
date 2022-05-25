package wooteco.subway.domain.fare.strategy.discount;

import java.util.Arrays;

public enum DiscountStrategyMapper {

    DEFAULT_STRATEGY(DefaultDiscountStrategy.getInstance()),
    BABY_STRATEGY(BabyDiscountStrategy.getInstance()),
    CHILD_STRATEGY(ChildDiscountStrategy.getInstance()),
    TEENAGER_STRATEGY(TeenagerDiscountStrategy.getInstance()),
    SENIOR_STRATEGY(SeniorDiscountStrategy.getInstance()),
    ;

    private final DiscountStrategy strategy;

    DiscountStrategyMapper(final DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public static DiscountStrategy findStrategyBy(final int age) {
        return Arrays.stream(values())
                .map(it -> it.strategy)
                .filter(it -> it.isMatch(age))
                .findFirst()
                .orElseThrow();
    }
}
