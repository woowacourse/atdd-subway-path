package wooteco.subway.domain.fare;

public class DefaultFare extends Fare {

    public DefaultFare(int distance, int extraFare) {
        super(distance, extraFare);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE_AMOUNT + extraFare;
    }
}
