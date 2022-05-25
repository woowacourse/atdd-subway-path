package wooteco.subway.domain.fare;

public class ExtraSurchargeFare extends OverChargeFare {

    private static final int SURCHARGE_MAXIMUM_DISTANCE = 10;
    private static final int MINIMUM_DISTANCE = 50;
    private static final int DEFAULT_UNIT = 5;
    private static final int EXTRA_UNIT = 8;

    @Override
    public int calculateFare(int distance, int extraFare) {
        return DEFAULT_FARE_AMOUNT
                + calculateOverFare(MINIMUM_DISTANCE, SURCHARGE_MAXIMUM_DISTANCE, DEFAULT_UNIT)
                + calculateOverFare(distance, MINIMUM_DISTANCE, EXTRA_UNIT)
                + extraFare;
    }

    @Override
    public boolean checkDistanceRange(int distance) {
        return MINIMUM_DISTANCE < distance;
    }
}
