package wooteco.subway.domain.fare.farepolicy;

public class BaseCalculateStrategy implements FareCalculateStrategy {

    public static final int BASE_FARE = 1250;

    @Override
    public int calculateFare(double distance) {
        return BASE_FARE;
    }
}
