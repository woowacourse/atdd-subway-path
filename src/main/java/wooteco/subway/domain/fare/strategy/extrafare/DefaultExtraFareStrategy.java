package wooteco.subway.domain.fare.strategy.extrafare;

import wooteco.subway.domain.Distance;

public class DefaultExtraFareStrategy implements ExtraFareStrategy {

    private static final ExtraFareStrategy INSTANCE = new DefaultExtraFareStrategy();

    private DefaultExtraFareStrategy() {
    }

    public static ExtraFareStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int calculate(final Distance distance) {
        return 0;
    }
}
