package wooteco.subway.domain.strategy.extrafare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.Distance;

public enum ExtraFareStrategyMapper {

    DEFAULT_DISTANCE(it -> it <= 10, DefaultExtraFareStrategy.getInstance()),
    TEN_DISTANCE(it -> 10 < it && it <= 50, TenExtraFareStrategy.getInstance()),
    FIFTY_DISTANCE(it -> 50 < it, FiftyExtraFareStrategy.getInstance()),
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
                .map(it -> it.strategy)
                .findFirst()
                .orElseThrow();
    }
}
