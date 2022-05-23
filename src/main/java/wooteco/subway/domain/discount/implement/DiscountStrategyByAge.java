package wooteco.subway.domain.discount.implement;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DiscountStrategyByAge {
    CHILDREN(it -> it >= 6 && it < 13, it -> (int)((it - 350)*0.5)),
    TEENAGER(it -> it >= 13 && it < 19, it -> (int)((it - 350)*0.8)),
    OTHER(it -> it < 6 || it >= 19, it -> it);

    private Predicate<Integer> condition;
    private UnaryOperator<Integer> strategy;

    DiscountStrategyByAge(Predicate<Integer> condition, UnaryOperator<Integer> strategy) {
        this.condition = condition;
        this.strategy = strategy;
    }

    public static int getDiscountFare(int age, int fare) {
        return Arrays.stream(DiscountStrategyByAge.values())
                .filter(it -> it.condition.test(age))
                .findAny()
                .orElse(OTHER)
                .strategy.apply(fare);
    }
}
