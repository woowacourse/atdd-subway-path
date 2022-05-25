package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.discountstrategy.AdultDiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.ChildDiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.DiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.FreeDiscountStrategy;
import wooteco.subway.domain.fare.discountstrategy.TeenagerDiscountStrategy;

public enum DiscountStrategyFactory {

    ADULT(age -> age >= 19 && age < 65, new AdultDiscountStrategy()),
    CHILD(age -> age >= 6 && age < 13, new ChildDiscountStrategy()),
    FREE(age -> age < 6 || age >= 65, new FreeDiscountStrategy()),
    TEENAGER(age -> age >= 13 && age < 19, new TeenagerDiscountStrategy());

    private static final String NOT_EXIST_MATCHED_AGE = "일치하는 나이가 없습니다.";

    private final Predicate<Integer> predicate;
    private final DiscountStrategy discountStrategy;

    DiscountStrategyFactory(Predicate<Integer> predicate, DiscountStrategy discountStrategy) {
        this.predicate = predicate;
        this.discountStrategy = discountStrategy;
    }

    public static DiscountStrategy getDiscountStrategy(int age) {
        return Arrays.stream(values())
                .filter(strategy -> strategy.predicate.test(age))
                .map(strategy -> strategy.discountStrategy)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MATCHED_AGE));
    }
}
