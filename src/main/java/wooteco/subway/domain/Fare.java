package wooteco.subway.domain;

public class Fare {

    private static final String NEGATIVE_ERROR = "거리는 음수가 될 수 없습니다.";
    private static final int FIRST_BASIC_FARE = 1250;
    private static final int SECOND_BASIC_FARE = 2050;
    private static final int FIRST_STANDARD_DISTANCE = 10;
    private static final int SECOND_STANDARD_DISTANCE = 50;
    private static final int FIRST_ADDED_STANDARD = 5;
    private static final int SECOND_ADDED_STANDARD = 8;
    private static final int ADDED_FARE = 100;

    private final int distance;

    public Fare(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException(NEGATIVE_ERROR);
        }
    }

    public int calculate() {
        if (distance <= FIRST_STANDARD_DISTANCE) {
            return FIRST_BASIC_FARE;
        }
        if (distance <= SECOND_STANDARD_DISTANCE) {
            return FIRST_BASIC_FARE
                    + calculateOverFare(distance - FIRST_STANDARD_DISTANCE, FIRST_ADDED_STANDARD);
        }
        return SECOND_BASIC_FARE
                + calculateOverFare(distance - SECOND_STANDARD_DISTANCE, SECOND_ADDED_STANDARD);
    }

    private int calculateOverFare(int distance, int addedStandard) {
        return (int) ((Math.ceil((distance - 1) / addedStandard) + 1) * ADDED_FARE);
    }
}
