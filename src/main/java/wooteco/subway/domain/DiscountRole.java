package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.strategy.discount.AdolescentDiscountStrategy;
import wooteco.subway.domain.strategy.discount.DiscountStrategy;
import wooteco.subway.domain.strategy.discount.KidDiscountStrategy;
import wooteco.subway.domain.strategy.discount.NonDiscountStrategy;

public enum DiscountRole {

    BABY(age -> age < 6, new NonDiscountStrategy()),
    KID(age -> age >= 6 && age < 13, new KidDiscountStrategy()),
    ADOLESCENT(age -> age >= 13 && age < 19, new AdolescentDiscountStrategy()),
    ADULT(age -> age > 19, new NonDiscountStrategy());

    private Predicate<Integer> predicate;
    private DiscountStrategy strategy;

    public static DiscountStrategy findDiscountStrategy(int age) {
        return Arrays.stream(values()).filter(role -> role.predicate.test(age)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("전략을 찾을 수 없습니다.")).getStrategy();
    }

    DiscountRole(Predicate<Integer> predicate, DiscountStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    private DiscountStrategy getStrategy() {
        return strategy;
    }
}
