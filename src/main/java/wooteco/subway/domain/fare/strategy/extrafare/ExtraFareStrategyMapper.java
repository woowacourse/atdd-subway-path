package wooteco.subway.domain.fare.strategy.extrafare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.Distance;

public enum ExtraFareStrategyMapper {

    DEFAULT_STRATEGY(ExtraFareStrategyMapper::isDefault, DefaultExtraFareStrategy.getInstance()),
    OVER_TEN_STRATEGY(ExtraFareStrategyMapper::isOverTen, TenExtraFareStrategy.getInstance()),
    OVER_FIFTY_STRATEGY(ExtraFareStrategyMapper::isOverFifty, FiftyExtraFareStrategy.getInstance()),
    ;

    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int EXTRA_FARE_DISTANCE = 50;
    
    private final Predicate<Integer> predicate;
    private final ExtraFareStrategy strategy;

    ExtraFareStrategyMapper(final Predicate<Integer> predicate, final ExtraFareStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    private static boolean isDefault(final Integer distance) {
        return distance <= DEFAULT_FARE_DISTANCE;
    }

    private static boolean isOverTen(final Integer distance) {
        return DEFAULT_FARE_DISTANCE < distance && distance <= EXTRA_FARE_DISTANCE;
    }

    private static boolean isOverFifty(final Integer distance) {
        return EXTRA_FARE_DISTANCE < distance;
    }

    public static ExtraFareStrategy findStrategyBy(final Distance distance) {
        return Arrays.stream(values())
                .filter(it -> it.predicate.test(distance.getValue()))
                .findFirst()
                .orElseThrow()
                .strategy;
    }
}
