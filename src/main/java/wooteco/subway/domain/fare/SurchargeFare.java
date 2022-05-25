package wooteco.subway.domain.fare;

public class SurchargeFare extends OverChargeFare {

    private static final int MINIMUM_DISTANCE = 10;
    private static final int MAXIMUM_DISTANCE = 50;
    private static final int DEFAULT_UNIT = 5;

    @Override
    public int calculateFare(int distance, int extraFare) {
        return DEFAULT_FARE_AMOUNT
                + calculateOverFare(distance, MINIMUM_DISTANCE, DEFAULT_UNIT)
                + extraFare;
    }

    @Override
    public boolean checkDistanceRange(int distance) {
        return MINIMUM_DISTANCE < distance && distance <= MAXIMUM_DISTANCE;
    }
}
