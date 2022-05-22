package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARES = 1250;
    private static final int SECOND_SECTION_FARES = 800;
    private static final int BASIC_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;
    private static final int SECOND_CHARGING_UNITS = 5;
    private static final int THIRD_CHARGING_UNITS = 8;
    private static final int UNIT_FARES = 100;
    private static final int MINIMUM_DISTANCE = 0;
    private static final int MINIMUM_EXTRA_FARE = 0;

    private Fare() {
    }

    public static int calculateFare(int distance, int extraFare) {
        validateZeroOrPositiveDistance(distance, extraFare);
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARES + extraFare;
        }
        if (distance <= SECOND_DISTANCE) {
            return BASIC_FARES + calculateOverFare(distance - BASIC_DISTANCE, SECOND_CHARGING_UNITS) + extraFare;
        }
        return BASIC_FARES + SECOND_SECTION_FARES + calculateOverFare(distance - SECOND_DISTANCE, THIRD_CHARGING_UNITS) + extraFare;
    }

    private static void validateZeroOrPositiveDistance(int distance, int extraFare) {
        if (distance <= MINIMUM_DISTANCE || extraFare <= MINIMUM_EXTRA_FARE) {
            throw new IllegalArgumentException("거리 또는 추가요금은 음수일 수 없습니다.");
        }
    }

    private static int calculateOverFare(int distance, int farePerKilometre) {
        return (int) ((Math.ceil((distance - 1) / farePerKilometre) + 1) * UNIT_FARES);
    }
}
