package wooteco.subway.domain;

public class FareCalculator {

    private static final int BASIS_FARE = 1_250;
    private static final int BASIC_FARE_OVER_50KM = 2_050;
    private static final int FIRST_FARE_INCREASE_STANDARD = 10;
    private static final int LAST_FARE_INCREASE_STANDARD = 50;
    private static final int FIRST_FARE_INCREASE_STANDARD_UNIT = 5;
    private static final int LAST_FARE_INCREASE_STANDARD_UNIT = 8;
    private static final int INCREASE_RATE = 100;

    public static int calculateFare(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException();
        }
        if (distance <= FIRST_FARE_INCREASE_STANDARD) {
            return BASIS_FARE;
        }
        if (distance <= LAST_FARE_INCREASE_STANDARD) {
            return BASIS_FARE + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - FIRST_FARE_INCREASE_STANDARD) / FIRST_FARE_INCREASE_STANDARD_UNIT);
        }
        return BASIC_FARE_OVER_50KM + INCREASE_RATE *
                (int) Math.ceil((double) (distance - LAST_FARE_INCREASE_STANDARD) / LAST_FARE_INCREASE_STANDARD_UNIT);
    }
}
