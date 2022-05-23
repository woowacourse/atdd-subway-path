package wooteco.subway.domain.fare;

public class ExtraSurchargeFare extends Fare {
    @Override
    public int calculateFare(int distance, int extraFare) {
        return DEFAULT_FARE_AMOUNT
                + calculateOverFare(MAXIMUM_EXTRA_DISTANCE - MAXIMUM_DEFAULT_DISTANCE, DEFAULT_UNIT)
                + calculateOverFare(distance - MAXIMUM_EXTRA_DISTANCE, EXTRA_UNIT)
                + extraFare;
    }
}
