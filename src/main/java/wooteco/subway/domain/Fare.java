package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int INCREASE_FARE = 100;

    private static final int FIRST_PROGRESSIVE_INTERVAL = 10;
    private static final int PER_DISTANCE_FIRST_INTERVAL = 5;
    private static final int MAX_FARE_FIRST_INTERVAL = 800;

    private static final int SECOND_PROGRESSIVE_INTERVAL = 50;
    private static final int PER_DISTANCE_SECOND_INTERVAL = 8;

    private static final int DEFAULT_DEDUCTION = 350;

    private static final int MIN_CHILD_AGE = 6;
    private static final int MAX_CHILD_AGE = 12;
    private static final double CHILD_POLICY_RATE = 0.5;

    private static final int MIN_TEENAGER_AGE = 13;
    private static final int MAX_TEENAGER_AGE = 18;
    private static final double TEENAGER_POLICY_RATE = 0.8;

    private final int distance;
    private final int overFare;
    private final int age;

    private Fare(int distance, int overFare, int age) {
        this.distance = distance;
        this.overFare = overFare;
        this.age = age;
    }

    public static Fare of(int distance, int overFare, int age) {
        return new Fare(distance, overFare, age);
    }

    public int calculate() {
        int basicFare = BASIC_FARE +
            getOverFare(FIRST_PROGRESSIVE_INTERVAL, PER_DISTANCE_FIRST_INTERVAL, MAX_FARE_FIRST_INTERVAL) +
            getOverFare(SECOND_PROGRESSIVE_INTERVAL, PER_DISTANCE_SECOND_INTERVAL, Integer.MAX_VALUE);
        return calculateAgeDiscounted(basicFare + overFare);
    }

    private int calculateAgeDiscounted(int fare) {
        if (isChildPolicy()) {
            return calculatePolicy(fare, CHILD_POLICY_RATE);
        }

        if (isTeenagerPolicy()) {
            return calculatePolicy(fare, TEENAGER_POLICY_RATE);
        }

        return fare;
    }

    private boolean isChildPolicy() {
        return age >= MIN_CHILD_AGE && age <= MAX_CHILD_AGE;
    }

    private boolean isTeenagerPolicy() {
        return age >= MIN_TEENAGER_AGE && age <= MAX_TEENAGER_AGE;
    }

    private int calculatePolicy(int fare, double policyRate) {
        return (int)((fare - DEFAULT_DEDUCTION) * policyRate);
    }

    private int getOverFare(int progressiveInterval, int perDistanceOverInterval, int MaxFareInterval) {
        if (distance > progressiveInterval) {
            return Math.min(MaxFareInterval,
                (int)(Math.ceil((double)(distance - progressiveInterval) / perDistanceOverInterval)) * INCREASE_FARE);
        }
        return 0;
    }
}
