package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.strategy.discount.AdolescentDiscountStrategy;
import wooteco.subway.domain.strategy.discount.DiscountStrategy;
import wooteco.subway.domain.strategy.discount.FreeDiscountStrategy;
import wooteco.subway.domain.strategy.discount.KidDiscountStrategy;
import wooteco.subway.domain.strategy.discount.NonDiscountStrategy;

public enum DiscountRole {

    BABY(age -> age < Constants.KID_STANDARD, new FreeDiscountStrategy()),
    KID(age -> age >= Constants.KID_STANDARD && age < Constants.ADOLESCENT_STANDARD,
            new KidDiscountStrategy()),
    ADOLESCENT(age -> age >= Constants.ADOLESCENT_STANDARD && age < Constants.ADULT_STANDARD,
            new AdolescentDiscountStrategy()),
    ADULT(age -> age > Constants.ADULT_STANDARD && age < Constants.SENIOR_STANDARD,
            new NonDiscountStrategy()),
    SENIOR(age -> age >= Constants.SENIOR_STANDARD, new FreeDiscountStrategy());

    private static class Constants {
        private static final int KID_STANDARD = 6;
        private static final int ADOLESCENT_STANDARD = 13;
        private static final int ADULT_STANDARD = 19;
        private static final int SENIOR_STANDARD = 65;
    }

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
