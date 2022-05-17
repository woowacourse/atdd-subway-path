package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int FIRST_SECTION_BASIC_FARE = 800;
    private static final int FIRST_SECTION_UNIT = 5;
    private static final int SECOND_SECTION_UNIT = 8;

    private final int fare;

    public Fare(int distance) {
        this.fare = calculateFare(distance);
    }

    private int calculateFare(int distance) {
        if (distance < 10) {
            return BASIC_FARE;
        }
        if (distance <= 50) {
            return calcAdditionalFare(distance - 10, fare, FIRST_SECTION_UNIT);
        }
        return FIRST_SECTION_BASIC_FARE + calcAdditionalFare(distance - 50, fare, SECOND_SECTION_UNIT);
    }

    private int calcAdditionalFare(int distance, int fare, int unit) {
        fare += (distance / unit) * 100;
        if (distance % unit > 0 || (distance / unit == 0)) {
            fare += 100;
        }
        return BASIC_FARE + fare;
    }

    public int getFare() {
        return fare;
    }
}
