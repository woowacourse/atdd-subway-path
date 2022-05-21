package wooteco.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int INCREASE_FARE = 100;

    private static final int FIRST_PROGRESSIVE_INTERVAL = 10;
    private static final int PER_DISTANCE_FIRST_INTERVAL = 5;
    private static final int MAX_FARE_FIRST_INTERVAL = 800;

    private static final int SECOND_PROGRESSIVE_INTERVAL = 50;
    private static final int PER_DISTANCE_SECOND_INTERVAL = 8;

    private final int distance;
    private final int overFare;
    private final int age;

    public Fare(int distance, int overFare, int age) {
        this.distance = distance;
        this.overFare = overFare;
        this.age = age;
    }

    private Fare(int distance) {
        this(distance, 0, 0);
    }

    public static Fare from(int distance) {
        return new Fare(distance);
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
        if (age >= 6 && age < 13) {
            return (int)((fare - 350) * 0.5);
        }

        if (age >= 13 && age < 19) {
            return (int)((fare - 350) * 0.8);
        }

        return fare;
    }

    private int getOverFare(int progressiveInterval, int perDistanceOverInterval, int MaxFareInterval) {
        if (distance > progressiveInterval) {
            return Math.min(MaxFareInterval,
                (int)(Math.ceil((double)(distance - progressiveInterval) / perDistanceOverInterval)) * INCREASE_FARE);
        }
        return 0;
    }
}
