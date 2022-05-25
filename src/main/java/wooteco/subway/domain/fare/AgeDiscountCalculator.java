package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscountCalculator {
    ADULT((age) -> age >= 19, (fare) -> fare),
    TEENAGER((age) -> age < 19 && age >= 13, (fare) -> (fare - 350) * 80 / 100),
    CHILDREN((age) -> age < 13 && age >= 6, (fare) -> (fare - 350) / 2),
    INFANT((age) -> age < 6 && age >= 0, (fare) -> 0);

    private final Predicate<Integer> condition;
    private final Function<Integer, Integer> calculate;

    AgeDiscountCalculator(Predicate<Integer> condition,
        Function<Integer, Integer> calculate) {
        this.condition = condition;
        this.calculate = calculate;
    }

    public static int calculateDiscount(int age, int totalFare) {
        return Arrays.stream(values())
            .filter(person -> person.condition.test(age))
            .map(person -> person.calculate.apply(totalFare))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재할 수 없는 연령입니다."));
    }
}
