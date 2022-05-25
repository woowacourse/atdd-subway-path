package wooteco.subway.domain.fare;

public class Fare {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_FARE_DISTANCE = 50;
    private static final int STANDARD_UNIT = 5;
    private static final int MAX_UNIT = 8;

    private final int fare;

    private Fare(int fare) {
        checkPositiveNumber(fare);
        this.fare = fare;
    }

    private void checkPositiveNumber(final int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 0원 이상이어야 합니다.");
        }
    }

    public static Fare of(final int distance, final int extraFare, final int age) {
        final int fare = calculateFare(distance, extraFare);
        final DiscountPolicyByAge discountPolicy = DiscountPolicyByAge.from
                (age);
        return new Fare(DiscountPolicyByAge.discount(fare, discountPolicy));
    }

    private static int calculateFare(final int distance, final int extraFare) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE + extraFare;
        }
        if (distance <= OVER_FARE_DISTANCE) {
            return DEFAULT_FARE + extraFare + calculateOverFare(distance - DEFAULT_DISTANCE, STANDARD_UNIT);
        }
        return DEFAULT_FARE
                + extraFare
                + calculateOverFare(OVER_FARE_DISTANCE - DEFAULT_DISTANCE, STANDARD_UNIT)
                + calculateOverFare(distance - OVER_FARE_DISTANCE, MAX_UNIT);
    }

    private static int calculateOverFare(final int distance, final int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
