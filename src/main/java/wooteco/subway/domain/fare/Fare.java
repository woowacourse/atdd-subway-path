package wooteco.subway.domain.fare;

public class Fare {
    static final int BASE_FEE = 1250;
    private static final int OVER_TEN_DISTANCE = 10;
    private static final int OVER_TEN_RATE = 5;
    private static final int OVER_FIFTY_DISTANCE = 50;
    private static final int OVER_FIFTY_RATE = 8;

    public Fare() {
    }

    public int getFare(int distance) {
        return calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if (distance <= OVER_TEN_DISTANCE) {
            return BASE_FEE;
        }
        if (distance <= OVER_FIFTY_DISTANCE) {
            return calculateOverFare(OVER_TEN_DISTANCE) + getAddedFare(distance, OVER_TEN_DISTANCE, OVER_TEN_RATE);
        }
        return calculateOverFare(OVER_FIFTY_DISTANCE) + getAddedFare(distance, OVER_FIFTY_DISTANCE, OVER_FIFTY_RATE);
    }

    private int getAddedFare(int distance, int overDistance, int overRate) {
        return (int) ((Math.ceil((distance - overDistance - 1) / overRate) + 1) * 100);
    }
}
