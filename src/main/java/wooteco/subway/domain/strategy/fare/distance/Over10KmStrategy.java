package wooteco.subway.domain.strategy.fare.distance;

import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.ADDITIONAL_FARE;
import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.BASIC_DISTANCE;
import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.STEP_ONE_CHARGE_DISTANCE;
import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.STEP_ONE_DISTANCE;

public class Over10KmStrategy implements FareDistanceStrategy {

    private static final int OVER_10KM_MINIMUM = 10;

    @Override
    public boolean isApplied(int distance) {
        return distance > OVER_10KM_MINIMUM;
    }

    @Override
    public int distanceFare(int distance) {
        return (int) Math.ceil(Math.min(distance - BASIC_DISTANCE, STEP_ONE_DISTANCE - BASIC_DISTANCE) / STEP_ONE_CHARGE_DISTANCE) * ADDITIONAL_FARE;
    }
}
