package wooteco.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.exception.FareStrategyNotFoundException;

public enum FareStrategyFactory {

    DEFAULT(FareStrategyFactory::isDefault, new DefaultFareStrategy()),
    FIRST_OVER(FareStrategyFactory::isFirstOver, new FirstOverFareStrategy()),
    MAX(FareStrategyFactory::isMax, new MaxFareStrategy());

    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_FARE_DISTANCE = 50;

    private final Predicate<Integer> predicate;

    private final FareStrategy fareStrategy;

    FareStrategyFactory(Predicate<Integer> predicate, FareStrategy fareStrategy) {
        this.predicate = predicate;
        this.fareStrategy = fareStrategy;
    }

    private static boolean isDefault(Integer distance) {
        return distance >= 1 && distance <= DEFAULT_DISTANCE;
    }
    private static boolean isFirstOver(Integer distance) {
        return distance > DEFAULT_DISTANCE && distance <= OVER_FARE_DISTANCE;
    }

    private static boolean isMax(Integer distance) {
        return distance > OVER_FARE_DISTANCE;
    }

    public static FareStrategy get(final int distance) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(distance))
                .findFirst()
                .orElseThrow(FareStrategyNotFoundException::new)
                .fareStrategy;
    }
}
