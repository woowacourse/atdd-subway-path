package wooteco.subway.domain.fare;

public class DefaultFare extends Fare {
    @Override
    public int calculateFare(int distance, int extraFare) {
        return DEFAULT_FARE_AMOUNT + extraFare;
    }
}
