package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import wooteco.subway.exception.DataNotExistException;

public enum AgeDiscountStrategy {

    BABY(age -> age >= 0 && age < 5, fare -> 0.0),
    CHILD(age -> age >= 5 && age < 13, fare -> (fare - 350) * 0.5),
    YOUTH(age -> age >= 13 && age < 19, fare -> (fare - 350) * 0.8),
    ADULT(age -> age >= 19, fare -> fare);

    private final IntPredicate ageBoundary;
    private final IntToDoubleFunction discountStrategy;

    AgeDiscountStrategy(IntPredicate ageBoundary, IntToDoubleFunction discountStrategy) {
        this.ageBoundary = ageBoundary;
        this.discountStrategy = discountStrategy;
    }

    public static AgeDiscountStrategy from(int age) {
        return Arrays.stream(values())
                .filter(it -> it.ageBoundary.test(age))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("존재하지 않는 나이입니다."));
    }

    public int discount(int fare) {
        return (int) discountStrategy.applyAsDouble(fare);
    }
}
