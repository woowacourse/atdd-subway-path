package wooteco.subway.domain.strategy.extrafare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.Distance;

public enum ExtraFareStrategyMapper {

    DEFAULT_STRATEGY(it -> it <= 10, DefaultExtraFareStrategy.getInstance()),
    OVER_TEN_STRATEGY(it -> 10 < it && it <= 50, TenExtraFareStrategy.getInstance()),
    OVER_FIFTY_STRATEGY(it -> 50 < it, FiftyExtraFareStrategy.getInstance()),
    ;

    private final Predicate<Integer> predicate;
    private final ExtraFareStrategy strategy;

    ExtraFareStrategyMapper(final Predicate<Integer> predicate, final ExtraFareStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    public static ExtraFareStrategy findStrategyBy(final Distance distance) {
        return Arrays.stream(values())
                .filter(it -> it.predicate.test(distance.getValue()))
                .findFirst()
                .orElseThrow()
                .strategy;
    }
}
