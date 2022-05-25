package wooteco.subway.domain.fare.farestrategy;

import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;

public class DistanceStrategy implements FareStrategy {
    private static final int MINIMUM_DISTANCE = 0;
    private static final int ADDITIONAL_FARE_MULTIPLIER = 100;
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int MAX_FIRST_EXTRA_FARE_DISTANCE = 50;
    private static final int FIRST_EXTRA_FARE_UNIT = 5;
    private static final int SECOND_EXTRA_FARE_UNIT = 8;

    private final int distance;

    public DistanceStrategy(int distance) {
        if (distance <= MINIMUM_DISTANCE) {
            throw new DomainException(ExceptionMessage.UNDER_MIN_DISTANCE.getContent());
        }
        this.distance = distance;
    }

    @Override
    public long calculate(long fare) {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= MAX_FIRST_EXTRA_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateOverFare(distance - DEFAULT_FARE_DISTANCE, FIRST_EXTRA_FARE_UNIT);
        }
        return DEFAULT_FARE
                + calculateOverFare(MAX_FIRST_EXTRA_FARE_DISTANCE - DEFAULT_FARE_DISTANCE, FIRST_EXTRA_FARE_UNIT)
                + calculateOverFare(distance - MAX_FIRST_EXTRA_FARE_DISTANCE, SECOND_EXTRA_FARE_UNIT);
    }

    private int calculateOverFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADDITIONAL_FARE_MULTIPLIER);
    }
}
