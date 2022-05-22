package wooteco.subway.domain;

public class Fare {

    private static final int FIRST_DEFAULT_FARE = 1250;
    private static final int SECOND_DEFAULT_FARE = 2050;
    private static final int FIRST_STANDARD_FARE_DISTANCE = 10;
    private static final int SECOND_STANDARD_FARE_DISTANCE = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_ADDITIONAL_STANDARD = 5;
    private static final int SECOND_ADDITIONAL_STANDARD = 8;
    private static final int DEDUCTION = 350;

    private final int distance;
    private final int age;
    private final int extraFare;

    public Fare(int distance, int age, int extraFare) {
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
    }

    public int calculateFare() {
        if (distance > FIRST_STANDARD_FARE_DISTANCE && distance <= SECOND_STANDARD_FARE_DISTANCE) {
            int fare = FIRST_DEFAULT_FARE + calculateFirstOverFare(distance - FIRST_STANDARD_FARE_DISTANCE);
            return reduceFare(fare + extraFare);
        }

        if (distance > SECOND_STANDARD_FARE_DISTANCE) {
            int fare = SECOND_DEFAULT_FARE + calculateSecondOverFare(distance - SECOND_STANDARD_FARE_DISTANCE);
            return reduceFare(fare + extraFare);
        }

        return reduceFare(FIRST_DEFAULT_FARE + extraFare);
    }

    private int calculateFirstOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / FIRST_ADDITIONAL_STANDARD) + 1) * ADDITIONAL_FARE);
    }

    private int calculateSecondOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / SECOND_ADDITIONAL_STANDARD) + 1) * ADDITIONAL_FARE);
    }

    private int reduceFare(int fare) {
        if (age >= 13 && age < 19) {
            return (int) ((fare - DEDUCTION) * 0.8);
        }

        if (age >= 6 && age < 13) {
            return (int) ((fare - DEDUCTION) * 0.5);
        }

        return fare;
    }
}
