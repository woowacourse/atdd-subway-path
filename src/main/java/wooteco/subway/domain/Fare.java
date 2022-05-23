package wooteco.subway.domain;

public class Fare {

    private final int distance;
    private final int extraFare;
    private final int age;

    private static final int BASIC_FARES = 1250;
    private static final int SECOND_SECTION_FARES = 800;
    private static final int BASIC_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;
    private static final int SECOND_CHARGING_UNITS = 5;
    private static final int THIRD_CHARGING_UNITS = 8;
    private static final int UNIT_FARES = 100;
    private static final int MINIMUM_DISTANCE = 0;
    private static final int MINIMUM_EXTRA_FARE = 0;
    private static final int DEDUCTED_FEE = 350;
    private static final int MINIMUM_AGE = 0;

    public Fare(final int distance, final int extraFare, final int age) {
        validateZeroOrPositiveDistance(distance, extraFare, age);
        this.distance = distance;
        this.extraFare = extraFare;
        this.age = age;
    }

    private void validateZeroOrPositiveDistance(int distance, int extraFare, int age) {
        if (distance <= MINIMUM_DISTANCE || extraFare < MINIMUM_EXTRA_FARE) {
            throw new IllegalArgumentException("거리 또는 추가요금은 음수일 수 없습니다.");
        }
        if (age < MINIMUM_AGE) {
            throw new IllegalArgumentException("나이는 음수일 수 없습니다.");
        }
    }

    public int calculateFare() {
        if (distance <= BASIC_DISTANCE) {
            return discountFare(BASIC_FARES + extraFare, age);
        }
        if (distance <= SECOND_DISTANCE) {
            return discountFare(BASIC_FARES + calculateOverFare(distance - BASIC_DISTANCE, SECOND_CHARGING_UNITS) + extraFare, age);
        }
        return discountFare(BASIC_FARES + SECOND_SECTION_FARES + calculateOverFare(distance - SECOND_DISTANCE, THIRD_CHARGING_UNITS) + extraFare, age);
    }

    private int discountFare(int fare, int age) {
        if (isTeenager(age)) {
            return calculateDiscountedFare(fare, 80);
        }
        if (isChildren(age)) {
            return calculateDiscountedFare(fare, 50);
        }
        return fare;
    }

    private int calculateDiscountedFare(int fare, int discountRate) {
        return ((fare - DEDUCTED_FEE) * discountRate) / 100 + DEDUCTED_FEE;
    }

    private boolean isChildren(int age) {
        return age >= 6 && age < 13;
    }

    private boolean isTeenager(int age) {
        return age >= 13 && age < 19;
    }

    private int calculateOverFare(int distance, int farePerKilometre) {
        return (int) ((Math.ceil((distance - 1) / farePerKilometre) + 1) * UNIT_FARES);
    }
}
