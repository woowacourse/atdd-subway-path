package wooteco.subway.domain;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final int DISTANCE_1 = 10;
    private static final int DISTANCE_2 = 50;
    private static final int UNIT_1 = 5;
    private static final int UNIT_2 = 8;

    private final int distance;
    private final int extraFare;

    public FareCalculator(int distance, int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public int calculate() {
        return calculateByDistance() + extraFare;
    }

    private int calculateByDistance() {
        int fare = BASIC_FARE;
        int distance = this.distance;

        if (distance > DISTANCE_2) {
            fare += calculateFare(distance, DISTANCE_2, UNIT_2);
            distance = DISTANCE_2;
        }
        if (distance > DISTANCE_1) {
            fare += calculateFare(distance, DISTANCE_1, UNIT_1);
        }
        return fare;
    }

    private int calculateFare(int distance, int paidDistance, int unit) {
        return ((distance - paidDistance - 1) / unit + 1) * EXTRA_FARE;
    }
}
