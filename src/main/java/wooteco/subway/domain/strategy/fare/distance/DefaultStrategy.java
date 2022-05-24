package wooteco.subway.domain.strategy.fare.distance;

import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.BASIC_FARE;

public class DefaultStrategy implements FareDistanceStrategy {

    private static final int DEFAULT_MINIMUM = 0;

    @Override
    public boolean isApplied(int distance) {
        return distance > DEFAULT_MINIMUM;
    }

    @Override
    public int distanceFare(int distance) {
        return BASIC_FARE;
    }
}
