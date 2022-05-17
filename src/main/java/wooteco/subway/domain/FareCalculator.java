package wooteco.subway.domain;

public class FareCalculator {

    private static final int SECOND_EXTRA_FARE_DISTANCE = 50;
    private static final int FIRST_EXTRA_FARE_STANDARD = 5;
    private static final int FIRST_EXTRA_FARE = 800;
    private static final int BASIC_FARE = 1250;
    private static final int FIRST_EXTRA_FARE_DISTANCE = 10;
    private static final int EXTRA_FARE = 100;
    private static final int SECOND_EXTRA_FARE_STANDARD = 8;

    private final double distance;

    public FareCalculator(double distance) {
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance > SECOND_EXTRA_FARE_DISTANCE) {
            return BASIC_FARE + FIRST_EXTRA_FARE + addSecondExtraFare(distance - SECOND_EXTRA_FARE_DISTANCE);
        }
        if (distance > FIRST_EXTRA_FARE_DISTANCE) {
            return BASIC_FARE + addFirstExtraFare(distance - FIRST_EXTRA_FARE_DISTANCE);
        }
        return BASIC_FARE;
    }

    private int addSecondExtraFare(double distance) {
        return (int) ((Math.ceil((distance) / SECOND_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }

    private int addFirstExtraFare(double distance) {
        return (int) ((Math.ceil((distance) / FIRST_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }
}
