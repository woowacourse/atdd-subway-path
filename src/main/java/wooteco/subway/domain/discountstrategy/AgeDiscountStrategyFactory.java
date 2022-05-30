package wooteco.subway.domain.discountstrategy;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum AgeDiscountStrategyFactory {

    CHILDREN_STRATEGY((age) -> age >= 6 && age < 13, ChildrenDiscountStrategy::new),
    TEENAGER_STRATEGY((age) -> age >= 13 && age < 19, TeenagerDiscountStrategy::new),
    DEFAULT_STRATEGY((age) -> false, DefaultDiscountStrategy::new);

    private final Predicate<Integer> predicate;
    private final Supplier<AgeDiscountStrategy> supplier;

    AgeDiscountStrategyFactory(Predicate<Integer> predicate, Supplier<AgeDiscountStrategy> supplier) {
        this.predicate = predicate;
        this.supplier = supplier;
    }

    public static AgeDiscountStrategy from(int age) {
        var strategy = Arrays.stream(values())
                .filter(it -> it.predicate.test(age))
                .findFirst()
                .orElse(DEFAULT_STRATEGY);

        return strategy.supplier.get();
    }
}
