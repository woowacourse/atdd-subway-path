package wooteco.subway.domain.strategy.discount;

import java.util.Arrays;
import java.util.function.Predicate;


public enum DiscountRole {

    BABY(age -> Constants.KID_STANDARD > age, new FreeDiscountStrategy()),
    KID(age -> Constants.KID_STANDARD <= age && age < Constants.ADOLESCENT_STANDARD,
            new KidDiscountStrategy()),
    ADOLESCENT(age -> Constants.ADOLESCENT_STANDARD <= age && age < Constants.ADULT_STANDARD,
            new AdolescentDiscountStrategy()),
    ADULT(age -> Constants.ADULT_STANDARD < age && age < Constants.SENIOR_STANDARD,
            new NonDiscountStrategy()),
    SENIOR(age -> Constants.SENIOR_STANDARD <= age, new FreeDiscountStrategy());

    private static class Constants {
        private static final int KID_STANDARD = 6;
        private static final int ADOLESCENT_STANDARD = 13;
        private static final int ADULT_STANDARD = 19;
        private static final int SENIOR_STANDARD = 65;
    }

    private Predicate<Integer> predicate;
    private DiscountStrategy strategy;

    DiscountRole(Predicate<Integer> predicate, DiscountStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    public static DiscountStrategy findDiscountStrategy(int age) {
        return Arrays.stream(values())
                .filter(role -> role.predicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("전략을 찾을 수 없습니다."))
                .getStrategy();
    }

    private DiscountStrategy getStrategy() {
        return strategy;
    }
}
