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
        if (distance <= FIRST_RESTRICTION_DISTANCE) {
            return BASIC_FARE;
        }
        if (distance <= SECOND_RESTRICTION_DISTANCE) {
            return BASIC_FARE + calculateFareOverFirstDistance(distance);
        }
        return BASIC_FARE
                + calculateFareOverFirstDistance(SECOND_RESTRICTION_DISTANCE)
                + calculateFareOverSecondDistance();
    }

    private int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_RESTRICTION_DISTANCE) / 5.0) * ADDITIONAL_FARE);
    }

    private int calculateFareOverSecondDistance() {
        return (int) (Math.ceil((distance - SECOND_RESTRICTION_DISTANCE) / 8.0) * ADDITIONAL_FARE);
    }
}
