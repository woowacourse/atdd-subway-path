package wooteco.subway.domain.fare;

public class ExtraSurchargeFare extends Fare {

    public ExtraSurchargeFare(int distance, int extraFare) {
        super(distance, extraFare);
    }

    @Override
    public int calculateFare() {
        return DEFAULT_FARE_AMOUNT
                + calculateOverFare(MAXIMUM_EXTRA_DISTANCE - MAXIMUM_DEFAULT_DISTANCE, DEFAULT_UNIT)
                + calculateOverFare(distance - MAXIMUM_EXTRA_DISTANCE, EXTRA_UNIT)
                + extraFare;
    }
}
