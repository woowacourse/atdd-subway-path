package wooteco.subway.domain.strategy.fare.distance;

import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.ADDITIONAL_FARE;
import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.STEP_ONE_DISTANCE;
import static wooteco.subway.domain.strategy.fare.distance.FareDistanceConstant.STEP_TWO_CHARGE_DISTANCE;

public class Over50KmStrategy implements FareDistanceStrategy {

    private static final int OVER_50KM_MINIMUM = 50;

    @Override
    public boolean isApplied(int distance) {
        return distance > OVER_50KM_MINIMUM;
    }

    @Override
    public int distanceFare(int distance) {
        return (int) Math.ceil((distance - STEP_ONE_DISTANCE) / STEP_TWO_CHARGE_DISTANCE) * ADDITIONAL_FARE;
    }
}
