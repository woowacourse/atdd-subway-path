package wooteco.subway.domain.fare.distance;

public class MaximumFareStrategy implements DistanceFareStrategy {

    private static final int BASE_FARE = 1250;
    private static final int MAXIMUM_DISTANCE_BOUNDARY = 50;
    private static final int ABOVE_FIFTY_KM_POLICY = 8;
    private static final int OVER_FARE_PRICE = 100;

    @Override
    public int calculate(final double distance) {
        return BASE_FARE + 800 + (int) ((Math.ceil((distance - MAXIMUM_DISTANCE_BOUNDARY) / ABOVE_FIFTY_KM_POLICY)) * OVER_FARE_PRICE);
    }
}
