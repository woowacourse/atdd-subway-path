package wooteco.subway.domain.discount.implement;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import wooteco.subway.domain.Age;

public enum DiscountStrategyByAge {
    CHILDREN(Age::isChildren, it -> (int)((it - 350)*0.5)),
    TEENAGER(Age::isTeenager, it -> (int)((it - 350)*0.8)),
    OTHER(Age::isOther, it -> it);

    private Predicate<Age> condition;
    private UnaryOperator<Integer> strategy;

    DiscountStrategyByAge(Predicate<Age> condition, UnaryOperator<Integer> strategy) {
        this.condition = condition;
        this.strategy = strategy;
    }

    public static int getDiscountFare(Age age, int fare) {
        return Arrays.stream(DiscountStrategyByAge.values())
                .filter(it -> it.condition.test(age))
                .findAny()
                .orElse(OTHER)
                .strategy.apply(fare);
    }
}
