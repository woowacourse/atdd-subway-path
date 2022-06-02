package wooteco.subway.domain.discount.age;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.Age;
import wooteco.subway.domain.discount.age.implement.ChildrenDiscount;
import wooteco.subway.domain.discount.age.implement.NoDiscount;
import wooteco.subway.domain.discount.age.implement.TeenagerDiscount;

public enum DiscountStrategyByAge {
    CHILDREN(Age::isChildren, new ChildrenDiscount()),
    TEENAGER(Age::isTeenager, new TeenagerDiscount()),
    OTHER(Age::isOther, new NoDiscount());

    private Predicate<Age> condition;
    private AgeDiscountStrategy strategy;

    DiscountStrategyByAge(Predicate<Age> condition, AgeDiscountStrategy strategy) {
        this.condition = condition;
        this.strategy = strategy;
    }

    public static AgeDiscountStrategy getDiscountFare(Age age) {
        return Arrays.stream(DiscountStrategyByAge.values())
                .filter(it -> it.condition.test(age))
                .findAny()
                .orElse(OTHER)
                .strategy;
    }
}
