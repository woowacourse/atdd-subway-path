package wooteco.subway.domain.strategy.fare.distance;


import static wooteco.subway.domain.strategy.fare.FarePolicy.ADDITIONAL_FARE;
import static wooteco.subway.domain.strategy.fare.FarePolicy.BASIC_DISTANCE;
import static wooteco.subway.domain.strategy.fare.FarePolicy.BASIC_FARE;
import static wooteco.subway.domain.strategy.fare.FarePolicy.STEP_ONE_CHARGE_DISTANCE;
import static wooteco.subway.domain.strategy.fare.FarePolicy.STEP_ONE_DISTANCE;

public class Over10KmStrategy implements FareDistanceStrategy {

    @Override
    public int distanceFare(int distance) {
        return BASIC_FARE
                + (int) Math.ceil(Math.min(distance - BASIC_DISTANCE, STEP_ONE_DISTANCE - BASIC_DISTANCE) / STEP_ONE_CHARGE_DISTANCE) * ADDITIONAL_FARE;
    }
}
