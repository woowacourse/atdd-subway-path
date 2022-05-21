package wooteco.subway.domain;

public class FareCalculator {

    private static final int DEFAULT_FARE = 1250;
    private static final int SURCHARGE_PER_UNIT = 100;
    private static final int FIRST_STANDARD = 10;
    private static final int SECOND_STANDARD = 50;
    private static final int FIRST_STANDARD_UNIT = 5;
    private static final int SECOND_STANDARD_UNIT = 8;
    private static final int FIRST_STANDARD_MAXIMUM_COUNT = 8;

    public static int calculate(int distance) {
        if (distance > SECOND_STANDARD) {
            return DEFAULT_FARE + SURCHARGE_PER_UNIT * FIRST_STANDARD_MAXIMUM_COUNT + (int) (
                    ((Math.ceil((distance - SECOND_STANDARD) / (double) SECOND_STANDARD_UNIT))) * SURCHARGE_PER_UNIT);
        }
        if (distance > FIRST_STANDARD) {
            return DEFAULT_FARE + (int) ((Math.ceil((distance - FIRST_STANDARD) / (double) FIRST_STANDARD_UNIT))
                    * SURCHARGE_PER_UNIT);
        }
        return DEFAULT_FARE;
    }
}
