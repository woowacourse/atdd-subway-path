package wooteco.subway.domain.fare.farestrategy.charge;

import wooteco.subway.domain.fare.farestrategy.ChargeStrategy;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;

public abstract class DistanceStrategy implements ChargeStrategy {
    protected static final int MINIMUM_DISTANCE = 0;
    protected static final int ADDITIONAL_FARE_MULTIPLIER = 100;
    protected static final int DEFAULT_FARE_DISTANCE = 10;
    protected static final int MAX_FIRST_EXTRA_FARE_DISTANCE = 50;
    protected static final int FIRST_EXTRA_FARE_UNIT = 5;
    protected static final int SECOND_EXTRA_FARE_UNIT = 8;

    protected int distance;

    public DistanceStrategy(int distance) {
        if (distance <= MINIMUM_DISTANCE) {
            throw new DomainException(ExceptionMessage.UNDER_MIN_DISTANCE.getContent());
        }
        this.distance = distance;
    }

    @Override
    public abstract long calculate(long fare);

    protected int calculateOverFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADDITIONAL_FARE_MULTIPLIER);
    }
}
