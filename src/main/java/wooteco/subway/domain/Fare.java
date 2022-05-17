package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;

    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance <= FIRST_DISTANCE) {
            return BASIC_FARE;
        }
        if (distance <= SECOND_DISTANCE) {
            return BASIC_FARE + calculateFareOverFirstDistance(distance);
        }
        return BASIC_FARE
                + calculateFareOverFirstDistance(SECOND_DISTANCE)
                + calculateFareOverSecondDistance();
    }

    private int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_DISTANCE) / 5.0) * 100);
    }

    private int calculateFareOverSecondDistance() {
        return (int) (Math.ceil((distance - SECOND_DISTANCE) / 8.0) * 100);
    }
}
