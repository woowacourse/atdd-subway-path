package wooteco.subway.domain.fare;

public class Fare {

    private final int amount;

    private Fare(int amount) {
        this.amount = amount;
    }

    public static Fare calculate(int distance, int extraFare, int age) {
        Integer fareByDistance = FareByDistance.calculateFareByDistance(distance);
        int total = fareByDistance + extraFare;
        return new Fare(FareByAge.calculateFareByAge(age, total));
    }

    public int getAmount() {
        return amount;
    }
}
