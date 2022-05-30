package wooteco.subway.domain.fare;

public class DefaultFare implements Fare {

    private static final int MINIMUM_DISTANCE = 0;
    private static final int MAXIMUM_DISTANCE = 10;

    @Override
    public int calculateFare(int distance, int extraFare) {
        return DEFAULT_FARE_AMOUNT + extraFare;
    }

    @Override
    public boolean checkDistanceRange(int distance) {
        return MINIMUM_DISTANCE < distance && distance <= MAXIMUM_DISTANCE;
    }
}
