package wooteco.subway.domain;

public class Fare {

    private static final int OVER_FARE = 100;
    private static final int BASIC_FARE = 1250;
    private static final int FIRST_DISTANCE = 10;
    private static final int SECOND_DISTANCE = 50;

    private final int distance;
    private final int extraCharge;
    private final int age;

    public Fare(int distance, int extraCharge, int age) {
        this.distance = distance;
        this.extraCharge = extraCharge;
        this.age = age;
    }

    public int calculate() {
        if (distance <= FIRST_DISTANCE) {
            return discount(BASIC_FARE + extraCharge);
        }
        if (distance <= SECOND_DISTANCE) {
            return discount(BASIC_FARE + calculateFareOverFirstDistance(distance) + extraCharge);
        }
        return discount(BASIC_FARE
                + calculateFareOverFirstDistance(SECOND_DISTANCE)
                + calculateFareOverSecondDistance()
                + extraCharge);
    }

    private int discount(int totalCharge) {
        if (6 <= age && age < 13) {
            return (int) ((totalCharge - 350) * 0.5);
        }
        if (age < 19) {
            return (int) ((totalCharge - 350) * 0.8);
        }
        return totalCharge;
    }

    private int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_DISTANCE) / 5.0) * OVER_FARE);
    }

    private int calculateFareOverSecondDistance() {
        return (int) (Math.ceil((distance - SECOND_DISTANCE) / 8.0) * OVER_FARE);
    }
}
