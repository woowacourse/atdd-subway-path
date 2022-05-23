package wooteco.subway.domain.fare;

public class SurchargeFare extends Fare {

    public SurchargeFare(int distance, int extraFare) {
        super(distance, extraFare);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE_AMOUNT
                + calculateOverFare(distance - MAXIMUM_DEFAULT_DISTANCE, DEFAULT_UNIT)
                + extraFare;
    }
}
