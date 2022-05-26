package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FareRangeStrategy {

    BASIC(distance -> distance <= 10 && distance > 0, distance -> 1250),
    SECOND(distance -> distance > 10 && distance <= 50, distance -> 1250 + calculateOverFare(distance - 10, 5)),
    THIRD(distance -> distance > 50, distance -> 2050 + calculateOverFare(distance - 50, 8));

    private final Function<Integer, Boolean> chargeDistance;
    private final Function<Integer, Integer> calculate;

    FareRangeStrategy(Function<Integer, Boolean> chargeDistance, Function<Integer, Integer> calculate) {
        this.chargeDistance = chargeDistance;
        this.calculate = calculate;
    }

    public static int getFare(int distance) {
        return Arrays.stream(values())
                .filter(it -> it.chargeDistance.apply(distance))
                .findFirst()
                .map(it -> it.calculate.apply(distance))
                .orElseThrow();
    }

    private static int calculateOverFare(int distance, int farePerKilometre) {
        return (int) ((Math.ceil((distance - 1) / farePerKilometre) + 1) * 100);
    }
}
