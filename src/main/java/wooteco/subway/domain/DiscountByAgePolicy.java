package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountByAgePolicy {

    BABY_POLICY(
            (fare) -> 0,
            (age) -> 1 <= age && age < 6
    ),
    KIDS_POLICY(
            (fare) -> (int) Math.ceil((fare - 350) * 0.8),
            (age) -> 6 <= age && age < 13
    ),
    TEENAGER_POLICY(
            (fare) -> (int) Math.ceil((fare - 350) * 0.5),
            (age) -> 13 <= age && age < 19
    ),
    ADULT_POLICY(
            (fare) -> fare,
            (age) -> 19 <= age
    );

    private final Function<Integer, Integer> discountFunction;
    private final Predicate<Integer> rangePredicate;

    DiscountByAgePolicy(final Function<Integer, Integer> discountFunction, final Predicate<Integer> rangePredicate) {
        this.discountFunction = discountFunction;
        this.rangePredicate = rangePredicate;
    }

    public static DiscountByAgePolicy find(final int age) {
        return Arrays.stream(values())
                .filter(it -> it.rangePredicate.test(age))
                .findFirst()
                .orElseThrow();
    }
}
