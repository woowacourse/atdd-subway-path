package wooteco.subway.domain.fare;

public class Fare {
    private final int distance;
    private final int age;

    public Fare(int distance, int age) {
        this.distance = distance;
        this.age = age;
    }

    public int calculateFare() {
        int baseFare = Distance.calculateFareBy(distance);
        return Age.discountBy(age, baseFare);
    }
}


