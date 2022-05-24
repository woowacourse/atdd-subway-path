package wooteco.subway.domain.fare.distance;

public class MediumFareStrategy implements DistanceFareStrategy {

    private static final int BASE_FARE = 1250;
    private static final int MEDIUM_DISTANCE_BOUNDARY = 10;
    private static final int BELOW_FIFTY_KM_POLICY = 5;
    private static final int OVER_FARE_PRICE = 100;

    @Override
    public int calculate(final double distance) {
        return BASE_FARE + (int) ((Math.ceil((distance - MEDIUM_DISTANCE_BOUNDARY) / BELOW_FIFTY_KM_POLICY)) * OVER_FARE_PRICE);
    }
}
