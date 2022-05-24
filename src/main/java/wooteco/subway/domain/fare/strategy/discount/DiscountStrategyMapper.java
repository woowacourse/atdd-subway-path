package wooteco.subway.domain.fare.strategy.discount;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountStrategyMapper {

    DEFAULT_STRATEGY(DiscountStrategyMapper::isDefault, DefaultDiscountStrategy.getInstance()),
    BABY_STRATEGY(DiscountStrategyMapper::isBaby, BabyDiscountStrategy.getInstance()),
    CHILD_STRATEGY(DiscountStrategyMapper::isChild, ChildDiscountStrategy.getInstance()),
    TEENAGER_STRATEGY(DiscountStrategyMapper::isTeenager, TeenagerDiscountStrategy.getInstance()),
    SENIOR_STRATEGY(DiscountStrategyMapper::isSenior, SeniorDiscountStrategy.getInstance()),
    ;

    private static final int CHILD_AGE_LOWER_BOUND = 6;
    private static final int CHILD_AGE_UPPER_BOUND = 12;
    private static final int TEENAGER_AGE_LOWER_BOUND = 13;
    private static final int TEENAGER_AGE_UPPER_BOUND = 18;
    private static final int SENIOR_AGE_LOWER_BOUND = 65;

    private final Predicate<Integer> predicate;
    private final DiscountStrategy strategy;

    DiscountStrategyMapper(final Predicate<Integer> predicate, final DiscountStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    private static boolean isDefault(final Integer age) {
        return TEENAGER_AGE_UPPER_BOUND < age && age < SENIOR_AGE_LOWER_BOUND;
    }

    private static boolean isBaby(final Integer age) {
        return age < CHILD_AGE_LOWER_BOUND;
    }

    private static boolean isChild(final Integer age) {
        return CHILD_AGE_LOWER_BOUND <= age && age <= CHILD_AGE_UPPER_BOUND;
    }

    private static boolean isTeenager(final Integer age) {
        return TEENAGER_AGE_LOWER_BOUND <= age && age <= TEENAGER_AGE_UPPER_BOUND;
    }

    private static boolean isSenior(final Integer age) {
        return SENIOR_AGE_LOWER_BOUND <= age;
    }

    public static DiscountStrategy findStrategyBy(final int age) {
        return Arrays.stream(values())
                .filter(it -> it.predicate.test(age))
                .findFirst()
                .orElseThrow()
                .strategy;
    }
}
