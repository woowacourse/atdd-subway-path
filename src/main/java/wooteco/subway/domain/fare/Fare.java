package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.age.AgePolicy;
import wooteco.subway.domain.fare.distance.DistancePolicy;

public class Fare {

    private final int price;

    public Fare(final int price) {
        this.price = price;
    }

    public static Fare from(final double distance, final int extraFare, final int age) {
        return new Fare(calculateFare(distance, extraFare, age));
    }

    private static int calculateFare(final double distance, final int extraFare, final int age) {
        final int fare = DistancePolicy.calculate(distance) + extraFare;
        return AgePolicy.discount(age, fare);
    }

    public int getPrice() {
        return price;
    }
}
