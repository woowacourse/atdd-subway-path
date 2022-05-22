package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_RESTRICTION_DISTANCE = 10;
    private static final int SECOND_RESTRICTION_DISTANCE = 50;
    private static final double FIRST_RESTRICTION_DIVIDE = 5.0;
    private static final double SECOND_RESTRICTION_DIVIDE = 8.0;
    private static final int ADDITIONAL_FARE = 100;

    private final int fare;

    public Fare(int extraFare) {
        this.fare = BASIC_FARE + extraFare;
    }

    public int calculateFare(int distance) {
        if (distance <= FIRST_RESTRICTION_DISTANCE) {
            return fare;
        }
        if (distance <= SECOND_RESTRICTION_DISTANCE) {
            return fare + calculateOverTenDistance(distance);
        }
        return fare + calculateOverTenDistance(SECOND_RESTRICTION_DISTANCE) + calculateOverFiftyDistance(distance);
    }

    private int calculateOverTenDistance(int distance) {
        return (int) ((Math.ceil((distance - FIRST_RESTRICTION_DISTANCE) / FIRST_RESTRICTION_DIVIDE))
                * ADDITIONAL_FARE);
    }

    private int calculateOverFiftyDistance(int distance) {
        return (int) ((Math.ceil((distance - SECOND_RESTRICTION_DISTANCE) / SECOND_RESTRICTION_DIVIDE))
                * ADDITIONAL_FARE);
    }
}
