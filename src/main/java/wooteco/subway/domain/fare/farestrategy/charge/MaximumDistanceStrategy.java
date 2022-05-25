package wooteco.subway.domain.fare.farestrategy.charge;

public class MaximumDistanceStrategy extends DistanceStrategy {

    public MaximumDistanceStrategy(int distance) {
        super(distance);
    }

    @Override
    public long calculate(long fare) {
        return fare + calculateOverFare(MAX_FIRST_EXTRA_FARE_DISTANCE - DEFAULT_FARE_DISTANCE, FIRST_EXTRA_FARE_UNIT)
                + calculateOverFare(distance - MAX_FIRST_EXTRA_FARE_DISTANCE, SECOND_EXTRA_FARE_UNIT);
    }
}
