package wooteco.subway.domain.fare.farestrategy.charge;

public class AdditionalDistanceStrategy extends DistanceStrategy {
    public AdditionalDistanceStrategy(int distance) {
        super(distance);
    }

    @Override
    public long calculate(long fare) {
        return fare + calculateOverFare(distance - DEFAULT_FARE_DISTANCE, FIRST_EXTRA_FARE_UNIT);
    }
}
