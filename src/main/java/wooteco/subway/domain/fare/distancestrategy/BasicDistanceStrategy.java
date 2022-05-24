package wooteco.subway.domain.fare.distancestrategy;

public class BasicDistanceStrategy implements DistanceStrategy {

    private static final int BASIC_FARE = 1250;

    @Override
    public int getPrice(int distance) {
        return BASIC_FARE;
    }
}
