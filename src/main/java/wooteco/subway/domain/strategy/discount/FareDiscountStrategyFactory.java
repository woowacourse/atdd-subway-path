package wooteco.subway.domain.strategy.discount;

import static wooteco.subway.domain.strategy.discount.FareDiscountStrategyFactory.Constants.ADULT_AGE_STANDARD;
import static wooteco.subway.domain.strategy.discount.FareDiscountStrategyFactory.Constants.CHILDREN_AGE_STANDARD;
import static wooteco.subway.domain.strategy.discount.FareDiscountStrategyFactory.Constants.TEENAGER_AGE_STANDARD;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareDiscountStrategyFactory {

    BABY(FareDiscountStrategyFactory::isBaby, new BabyDiscountStrategy()),
    CHILD(FareDiscountStrategyFactory::isChildren, new ChildrenDiscountStrategy()),
    TEENAGER(FareDiscountStrategyFactory::isTeenager, new TeenagerDiscountStrategy()),
    ADULT(FareDiscountStrategyFactory::isAdult, new AdultDiscountStrategy());

    private final Predicate<Integer> matcher;
    private final FareDiscountStrategy fareDiscountStrategy;

    FareDiscountStrategyFactory(Predicate<Integer> matcher,
                                FareDiscountStrategy fareDiscountStrategy) {
        this.matcher = matcher;
        this.fareDiscountStrategy = fareDiscountStrategy;
    }

    private static boolean isBaby(Integer age) {
        return age < CHILDREN_AGE_STANDARD;
    }

    private static boolean isChildren(Integer age) {
        return age >= CHILDREN_AGE_STANDARD && age < TEENAGER_AGE_STANDARD;
    }

    private static boolean isTeenager(Integer age) {
        return age >= TEENAGER_AGE_STANDARD && age < ADULT_AGE_STANDARD;
    }

    private static boolean isAdult(Integer age) {
        return age >= ADULT_AGE_STANDARD;
    }

    public static FareDiscountStrategy getFareDiscountStrategy(int age) {
        return Arrays.stream(values())
                .filter(value -> value.matcher.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 연령에 해당하는 할인 정책을 찾을 수 없습니다."))
                .fareDiscountStrategy;
    }

    static class Constants {
        protected static final int CHILDREN_AGE_STANDARD = 6;
        protected static final int TEENAGER_AGE_STANDARD = 13;
        protected static final int ADULT_AGE_STANDARD = 19;
    }
}
