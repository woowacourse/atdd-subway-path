package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FareRangeStrategy {

    BASIC(distance -> 0 < distance && distance <= 10, distance -> Constants.BASIC_FARE),
    SECOND(distance -> 10 < distance && distance <= 50, distance -> Constants.BASIC_FARE + calculateOverFare(distance.subtract(new Distance(10)), new Distance(5))),
    THIRD(distance -> distance > 50, distance -> Constants.BASIC_FARE + Constants.SECOND_SECTION_FARE + calculateOverFare(distance.subtract(new Distance(50)), new Distance(8)));

    private final Function<Integer, Boolean> chargeDistance;
    private final Function<Distance, Integer> calculate;

    private static final int CHARGING_UNIT = 100;

    FareRangeStrategy(Function<Integer, Boolean> chargeDistance, Function<Distance, Integer> calculate) {
        this.chargeDistance = chargeDistance;
        this.calculate = calculate;
    }

    public static int getFare(final Distance distance) {
        return Arrays.stream(values())
                .filter(it -> it.chargeDistance.apply(distance.getValue()))
                .findFirst()
                .map(it -> it.calculate.apply(distance))
                .orElseThrow();
    }

    private static int calculateOverFare(final Distance distance, final Distance farePerKilometre) {
        return (int) ((Math.ceil((distance.getValue() - 1) / farePerKilometre.getValue()) + 1) * CHARGING_UNIT);
    }

    private static class Constants {
        private static final int BASIC_FARE = 1250;
        public static final int SECOND_SECTION_FARE = 800;
    }
}
