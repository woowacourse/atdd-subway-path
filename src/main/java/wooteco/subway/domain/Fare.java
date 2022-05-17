package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_SECTION_UNIT = 5;
    private static final int SECOND_SECTION_UNIT = 8;
    private static final int FIRST_SECTION_BOUNDARY = 10;
    private static final int SECOND_SECTION_BOUNDARY = 50;
    private static final int FIRST_SECTION_BASIC_FARE = 800;
    private final int fare;

    public Fare(int distance) {
        this.fare = calculateFare(distance);
    }

    private int calculateFare(int distance) {
        int fare = BASIC_FARE;
        if (distance >= FIRST_SECTION_BOUNDARY && distance <= SECOND_SECTION_BOUNDARY) {
            fare = calcAdditionalFare(distance, fare, FIRST_SECTION_BOUNDARY, FIRST_SECTION_UNIT);
        }
        if (distance > SECOND_SECTION_BOUNDARY) {
            fare = FIRST_SECTION_BASIC_FARE + calcAdditionalFare(distance, fare, SECOND_SECTION_BOUNDARY, SECOND_SECTION_UNIT);
        }
        return fare;
    }

    private int calcAdditionalFare(int distance, int fare, int boundary, int unit) {
        fare += ((distance - boundary) / unit) * ADDITIONAL_FARE;
        if ((distance - boundary) % unit > 0 || ((distance - boundary) / unit == 0)) {
            fare += ADDITIONAL_FARE;
        }
        return fare;
    }

    public int getFare() {
        return fare;
    }
}
