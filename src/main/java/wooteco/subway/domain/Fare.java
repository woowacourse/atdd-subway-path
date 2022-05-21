package wooteco.subway.domain;

public class Fare {

    private static final int STANDARD_FEE = 1250;
    private static final int SECOND_ADDITION_FEE = 800;
    private static final int STANDARD_DISTANCE = 10;
    private static final int SECOND_ADDITION_DISTANCE = 50;
    private static final int SECOND_ADDITION_FEE_UNIT = 5;
    private static final int THIRD_ADDITION_FEE_UNIT = 8;
    private static final int ADDITION_UNIT_FEE = 100;

    public int calculate(int distance) {
        if (distance <= STANDARD_DISTANCE) {
            return STANDARD_FEE;
        }
        if (distance <= SECOND_ADDITION_DISTANCE) {
            return STANDARD_FEE + calculateOverFare(distance - STANDARD_DISTANCE,
                SECOND_ADDITION_FEE_UNIT);
        }
        return STANDARD_FEE + SECOND_ADDITION_FEE + calculateOverFare(
            distance - SECOND_ADDITION_DISTANCE, THIRD_ADDITION_FEE_UNIT);
    }

    private int calculateOverFare(int distance, int farePerKilometre) {
        return (((distance - 1) / farePerKilometre) + 1) * ADDITION_UNIT_FEE;
    }
}
