package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum BasicFare {

    GRATER_THAN_50((distance) -> distance > Constants.GRATER_THAN_50_BOUND, BasicFare::calculateGraterThan50),
    GRATER_THAN_10((distance) -> distance > Constants.GRATER_THAN_10_BOUND, BasicFare::calculateGraterThan10),
    BASIC((distance) -> true, (distance) -> Constants.BASIC_FARE);

    private static class Constants {

        private static final int BASIC_FARE = 1_250;
        private static final int GRATER_THAN_50_BOUND = 50;
        private static final int GRATER_THAN_10_BOUND = 10;
        private static final int GRATER_THAN_50_UNIT_KILO = 8;
        private static final int GRATER_THAN_10_UNIT_KILO = 5;
        private static final int OVER_UNIT_FARE = 100;
    }

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    BasicFare(final Predicate<Integer> predicate, final Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int calculate(final int distance) {
        return find(distance).function.apply(distance);
    }

    private static BasicFare find(final int distance) {
        return Arrays.stream(BasicFare.values())
                .filter(v -> v.predicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 거리로 요금을 계산할 수 있는 방법이 없습니다."));
    }

    private static int calculateGraterThan50(final int distance) {
        return Constants.BASIC_FARE
                + calculateOverFare(Constants.GRATER_THAN_50_BOUND - Constants.GRATER_THAN_10_BOUND,
                Constants.GRATER_THAN_10_UNIT_KILO)
                + calculateOverFare(distance - Constants.GRATER_THAN_50_BOUND, Constants.GRATER_THAN_50_UNIT_KILO);
    }

    private static int calculateGraterThan10(final int distance) {
        return Constants.BASIC_FARE
                + calculateOverFare(distance - Constants.GRATER_THAN_10_BOUND, Constants.GRATER_THAN_10_UNIT_KILO);
    }


    private static int calculateOverFare(final int distance, final int kilo) {
        return (int) ((Math.ceil((distance - 1) / kilo) + 1) * Constants.OVER_UNIT_FARE);
    }
}
