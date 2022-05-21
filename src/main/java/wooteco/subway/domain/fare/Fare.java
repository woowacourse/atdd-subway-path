package wooteco.subway.domain.fare;

public class Fare {
    private final int distance;
    private final int age;
    private final int extraFare;

    private Fare(int distance, int age, int extraFare) {
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
    }

    public static Fare of(int distance, int age, int extraFare) {
        return new Fare(distance, age, extraFare);
    }

    public int calculateFare() {
        int baseFare = Distance.calculateFareBy(distance) + extraFare;
        return Age.discountBy(age, baseFare);
    }
}


