package wooteco.subway.domain.fare.strategy.extrafare;

import java.util.Arrays;
import wooteco.subway.domain.Distance;

public enum ExtraFareStrategyMapper {

    DEFAULT_STRATEGY(DefaultExtraFareStrategy.getInstance()),
    OVER_TEN_STRATEGY(TenExtraFareStrategy.getInstance()),
    OVER_FIFTY_STRATEGY(FiftyExtraFareStrategy.getInstance()),
    ;

    private final ExtraFareStrategy strategy;

    ExtraFareStrategyMapper(final ExtraFareStrategy strategy) {
        this.strategy = strategy;
    }

    public static ExtraFareStrategy findStrategyBy(final Distance distance) {
        return Arrays.stream(values())
                .map(it -> it.strategy)
                .filter(it -> it.isMatch(distance.getValue()))
                .findFirst()
                .orElseThrow();
    }
}
