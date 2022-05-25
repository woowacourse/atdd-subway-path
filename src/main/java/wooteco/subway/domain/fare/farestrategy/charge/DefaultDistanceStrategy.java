package wooteco.subway.domain.fare.farestrategy.charge;

import wooteco.subway.domain.fare.farestrategy.ChargeStrategy;

public class DefaultDistanceStrategy extends DistanceStrategy {

    public DefaultDistanceStrategy(int distance) {
        super(distance);
    }

    @Override
    public long calculate(long fare) {
        return fare;
    }
}
