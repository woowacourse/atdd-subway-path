package wooteco.subway.domain.fare;

public class SurchargeFare extends Fare {

    @Override
    public int calculateFare(int distance, int extraFare) {
        return DEFAULT_FARE_AMOUNT
                + calculateOverFare(distance - MAXIMUM_DEFAULT_DISTANCE, DEFAULT_UNIT)
                + extraFare;
    }
}
