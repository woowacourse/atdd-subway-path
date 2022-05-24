package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareByDistance {
    BASIC(distance -> distance <= 10, distance -> 1250),
    TENTOFIFTY(distance -> distance >= 11 && distance <= 50, distance -> 1250 + Math.max(calculateOverFromTenToFifty(distance - 10), 0)),
    OVERFIFTY(distance -> distance >= 51, distance -> 2050 + calculateOverFifty(distance - 50));

    private Predicate<Integer> predicate;
    private Function<Integer, Integer> function;

    FareByDistance(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static Integer calculateFareByDistance(int distance) {
        return (int) Math.ceil(Arrays.stream(FareByDistance.values())
                .filter(it -> it.predicate.test(distance))
                .findFirst()
                .map(it -> it.function.apply(distance))
                .orElseThrow());
    }

    private static int calculateOverFromTenToFifty(double distance) {
        return (int) ((Math.ceil(distance / 5) * 100));
    }

    private static int calculateOverFifty(double overDistance) {
        return (int) ((Math.ceil(overDistance / 8) * 100));
    }
}
