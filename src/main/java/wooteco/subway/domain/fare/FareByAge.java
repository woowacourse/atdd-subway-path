package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.PositiveDigitException;

public enum FareByAge {

    FREE_AGE(age -> (age >= 65 || age <= 5) && age > 0, fare -> 0),
    CHILDREN(age -> age > 5 && age < 13, FareByAge::calculateChildrenFare),
    TEENAGER(age -> age > 12 && age < 19, FareByAge::calculateTeenagerFare),
    ADULT(age -> age > 18 && age < 65, fare -> fare);

    private static final int DEFAULT_FARE = 350;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    FareByAge(final Predicate<Integer> predicate, final Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int findFare(final int age, final int fare) {
        return Arrays.stream(values())
                .filter(fareByAge -> fareByAge.predicate.test(age))
                .findFirst()
                .orElseThrow(() -> new PositiveDigitException("나이는 양수여야 합니다."))
                .function
                .apply(fare);
    }

    private static int calculateChildrenFare(final int fare) {
        return (int) ((fare - DEFAULT_FARE) * CHILDREN_DISCOUNT_RATE);
    }

    private static int calculateTeenagerFare(final int fare) {
        return (int) ((fare - DEFAULT_FARE) * TEENAGER_DISCOUNT_RATE);
    }

}
