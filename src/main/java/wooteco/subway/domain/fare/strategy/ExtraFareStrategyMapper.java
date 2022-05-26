package wooteco.subway.domain.fare.strategy;

import java.util.Arrays;

public enum ExtraFareStrategyMapper {

    NON(new NonExtraFareStrategy()),
    BASIC(new BasicExtraFareStrategy()),
    SPECIAL(new SpecialExtraFareStrategy());

    private ExtraFareStrategy strategy;

    ExtraFareStrategyMapper(ExtraFareStrategy strategy) {
        this.strategy = strategy;
    }

    public static ExtraFareStrategy findExtraFareStrategy(int distance) {
        return Arrays.stream(values())
                .filter(strategy -> strategy.strategy.isMatch(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("전략을 찾을 수 없습니다."))
                .getStrategy();
    }

    private ExtraFareStrategy getStrategy() {
        return strategy;
    }
}
