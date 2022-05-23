package wooteco.subway.domain;

public class Fare {

    private static final String NEGATIVE_ERROR = "거리는 음수가 될 수 없습니다.";
    private static final int FIRST_BASIC_FARE = 1250;
    private static final int SECOND_BASIC_FARE = 2050;
    private static final int FIRST_ADDED_STANDARD = 5;
    private static final int SECOND_ADDED_STANDARD = 8;
    private static final int ADDED_FARE = 100;
    private static final int DEDUCTION_FARE = 350;

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

    public int calculate(int extraFare, int age) {
        if (age < 6 || age >= 65) {
            return 0;
        }
        int fare = calculateWithDistance() + extraFare;
        if (age < 13) {
            return (fare - DEDUCTION_FARE) * 50 / 100;
        }
        if (age < 19) {
            return (fare - DEDUCTION_FARE) * 80 / 100;
        }
        return fare;
    }

    private int calculateWithDistance() {
        if (distance <= 10) {
            return FIRST_BASIC_FARE;
        }
        if (distance <= 50) {
            return FIRST_BASIC_FARE + calculateOverFare(distance - 10, FIRST_ADDED_STANDARD);
        }
        return SECOND_BASIC_FARE + calculateOverFare(distance - 50, SECOND_ADDED_STANDARD);
    }

    private int calculateOverFare(int distance, int addedStandard) {
        return (int) ((Math.ceil((distance - 1) / addedStandard) + 1) * ADDED_FARE);
    }
}
