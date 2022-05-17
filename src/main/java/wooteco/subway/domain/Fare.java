package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARES = 1250;
    private static final int SECOND_SECTION_FARES = 800;
    private static final int BASIC_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;
    private static final int SECOND_CHARGING_UNITS = 5;
    private static final int THIRD_CHARGING_UNITS = 8;
    private static final int UNIT_FARES = 100;

    public int calculateFare(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARES;
        }
        if (distance <= SECOND_DISTANCE) {
            return BASIC_FARES + calculateOverFare(distance - BASIC_DISTANCE, SECOND_CHARGING_UNITS);
        }
        return BASIC_FARES + SECOND_SECTION_FARES + calculateOverFare(distance - SECOND_DISTANCE, THIRD_CHARGING_UNITS);
    }

    private int calculateOverFare(int distance, int farePerKilometre) {
        return (int) ((Math.ceil((distance - 1) / farePerKilometre) + 1) * UNIT_FARES);
    }
}
