package wooteco.subway.domain.strategy.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum ExtraFareRole {

    NON(distance -> Constants.NO_EXTRA_STANDARD_DISTANCE >= distance, new NonExtraFare()),
    BASIC(distance -> Constants.NO_EXTRA_STANDARD_DISTANCE < distance
            && Constants.BASIC_EXTRA_FARE_MAX_DISTANCE >= distance, new BasicExtraFareStrategy()),
    SPECIAL(distance -> Constants.BASIC_EXTRA_FARE_MAX_DISTANCE < distance, new SpecialExtraFareStrategy());

    static class Constants {
        public static final int NO_EXTRA_STANDARD_DISTANCE = 10;
        private static final int BASIC_EXTRA_FARE_MAX_DISTANCE = 50;
    }

    private Predicate<Integer> predicate;
    private ExtraFareStrategy strategy;

    ExtraFareRole(Predicate<Integer> predicate, ExtraFareStrategy strategy) {
        this.predicate = predicate;
        this.strategy = strategy;
    }

    public static ExtraFareStrategy findExtraFareStrategy(int distance) {
        return Arrays.stream(values())
                .filter(role -> role.predicate.test(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("전략을 찾을 수 없습니다."))
                .getStrategy();
    }

    private ExtraFareStrategy getStrategy() {
        return strategy;
    }
}
