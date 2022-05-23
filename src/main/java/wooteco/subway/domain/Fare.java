package wooteco.subway.domain;

public class Fare {

    private static final int OVER_FARE = 100;
    private static final int BASIC_FARE = 1250;
    private static final int FIRST_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;

    private final int distance;
    private final int extraCharge;

    public Fare(int distance, int extraCharge) {
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public int calculate() {
        if (distance <= FIRST_DISTANCE) {
            return BASIC_FARE + extraCharge;
        }
        if (distance <= SECOND_DISTANCE) {
            return BASIC_FARE + calculateFareOverFirstDistance(distance) + extraCharge;
        }
        return BASIC_FARE
                + calculateFareOverFirstDistance(SECOND_DISTANCE)
                + calculateFareOverSecondDistance()
                + extraCharge;
    }

    private int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_DISTANCE) / 5.0) * OVER_FARE);
    }

    private int calculateFareOverSecondDistance() {
        return (int) (Math.ceil((distance - SECOND_DISTANCE) / 8.0) * OVER_FARE);
    }
}
