package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountFareCalculator {
    UNDER_SIX(age -> age < 6, fare -> 0),
    UNDER_THIRTEEN(age -> age < 13, fare -> (int)(0.5 * fare + 175)),
    UNDER_NINETEEN(age -> age < 19, fare -> (int)(0.8 * fare + 70));

    private final Predicate<Integer> condition;
    private final Function<Integer, Integer> fareOf;

    DiscountFareCalculator(Predicate<Integer> condition, Function<Integer, Integer> fareOf) {
        this.condition = condition;
        this.fareOf = fareOf;
    }

    public static int gerFare(int inputAge, int fare) {
        return Arrays.stream(DiscountFareCalculator.values())
                .filter(discountFareCalculator -> discountFareCalculator.condition.test(inputAge))
                .map(discountFareCalculator -> discountFareCalculator.fareOf.apply(fare))
                .findFirst()
                .orElse(fare);
    }
}
