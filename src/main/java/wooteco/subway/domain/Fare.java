package wooteco.subway.domain;

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

    int calculateOverFare(int distance) {
        if (distance <= OVER_TEN_DISTANCE) {
            return BASE_FEE;
        }
        if (distance <= OVER_FIFTY_DISTANCE) {
            return getAddedFare(distance, OVER_TEN_DISTANCE, OVER_TEN_RATE) + BASE_FEE;
        }
        return getAddedFare(distance, OVER_FIFTY_DISTANCE, OVER_FIFTY_RATE) + 2050;
    }

    private int getAddedFare(int distance, int overTenDistance, int overTenRate) {
        return (int) ((Math.ceil((distance - overTenDistance - 1) / overTenRate) + 1) * 100);
    }
}
