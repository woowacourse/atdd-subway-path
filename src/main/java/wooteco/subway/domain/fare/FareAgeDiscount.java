package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareAgeDiscount {

    BABY((age) -> age < Constants.BABY_BOUND, (fare) -> Constants.BABY_FARE),
    CHILD((age) -> age < Constants.CHILD_BOUND, (fare) -> getDiscountedFare(fare, Constants.CHILD_RATIO)),
    TEENAGER((age) -> age < Constants.TEENAGER_BOUND, (fare) -> getDiscountedFare(fare, Constants.TEENAGER_RATIO)),
    ADULT((age) -> true, (fare) -> fare);

    private static class Constants {

        private final static int BABY_BOUND = 6;
        private final static int CHILD_BOUND = 13;
        private final static int TEENAGER_BOUND = 19;
        private final static int BABY_FARE = 0;
        private final static double CHILD_RATIO = 0.5;
        private final static double TEENAGER_RATIO = 0.2;
    }

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    FareAgeDiscount(final Predicate<Integer> predicate, final Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int calculate(final int fare, final int age) {
        return find(age).function.apply(fare);
    }

    private static FareAgeDiscount find(final int age) {
        return Arrays.stream(FareAgeDiscount.values())
                .filter(v -> v.predicate.test(age))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("적용될 수 있는 나이 할인 정책이 없습니다."));
    }

    private static int getDiscountedFare(final int fare, final double ratio) {
        return fare - (int) ((fare - 350) * ratio);
    }
}
