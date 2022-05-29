package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareByDistance {
    BASIC(distance -> distance <= 10, distance -> 1250),
    TENTOFIFTY(distance -> distance >= 11 && distance <= 50, distance -> 1250 + calculateOverFromTenToFifty(distance)),
    OVERFIFTY(distance -> distance >= 51, distance -> 2050 + calculateOverFifty(distance));

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
        double overDistance = distance - 10;
        return (int) ((Math.ceil(overDistance / 5) * 100));
    }

    private static int calculateOverFifty(double distance) {
        double overDistance = distance - 50;
        return (int) ((Math.ceil(overDistance / 8) * 100));
    }
}
