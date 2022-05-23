package wooteco.subway.domain.fare;

public class Fare {
    private static final int BASE_FARE = 1250;

    private final int distance;
    private final int age;
    private final int extraFare;
    private final DiscountStrategy discountStrategy;

    public Fare(int distance, int age, int extraFare, DiscountStrategy discountStrategy) {
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
        this.discountStrategy = discountStrategy;
    }

    public int calculateFare() {
        int fare = BASE_FARE
                + extraFare
                + calculateFirstExtraFare(distance - 10)
                + calculateSecondExtraFare(distance - 50);

        return discountStrategy.discount(Age.from(age), fare);
    }

    private int calculateFirstExtraFare(int distanceInFirstRange) {
        if (distanceInFirstRange <= 0) {
            return 0;
        }
        return ((distanceInFirstRange - 1) / 5 + 1) * 100;
    }

    private int calculateSecondExtraFare(int distanceInSecondRange) {
        if (distanceInSecondRange <= 0) {
            return 0;
        }
        return ((distanceInSecondRange - 1) / 8 + 1) * 100;
    }
}


