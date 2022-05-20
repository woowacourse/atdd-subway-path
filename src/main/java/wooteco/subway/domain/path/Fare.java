package wooteco.subway.domain.path;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_RESTRICTION_DISTANCE = 10;
    private static final int SECOND_RESTRICTION_DISTANCE = 50;

    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        return BASIC_FARE + calculateFareOverFirstRestrictionDistance() + calculateFareOverSecondRestrictionDistance();
    }

    private int calculateFareOverFirstRestrictionDistance() {
        if (distance <= SECOND_RESTRICTION_DISTANCE) {
            return calculateAdditionalFare(distance, FIRST_RESTRICTION_DISTANCE, 5.0);
        }
        return calculateAdditionalFare(SECOND_RESTRICTION_DISTANCE, FIRST_RESTRICTION_DISTANCE, 5.0);
    }

    private int calculateFareOverSecondRestrictionDistance() {
        return calculateAdditionalFare(distance, SECOND_RESTRICTION_DISTANCE, 8.0);
    }

    private int calculateAdditionalFare(int distance, int restrictionDistance, double standardDistance) {
        if (distance <= restrictionDistance) {
            return 0;
        }
        return (int) (Math.ceil((distance - restrictionDistance) / standardDistance) * ADDITIONAL_FARE);
    }
}
