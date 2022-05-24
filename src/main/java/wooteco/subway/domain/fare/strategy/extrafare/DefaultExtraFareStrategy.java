package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public final class DefaultExtraFareStrategy extends ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new DefaultExtraFareStrategy();
    private static final int MAX_DISTANCE = 10;

    private DefaultExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isMatch(final int distance) {
        return distance <= MAX_DISTANCE;
    }

    @Override
    public int apply(final Distance distance) {
        return 0;
    }
}
