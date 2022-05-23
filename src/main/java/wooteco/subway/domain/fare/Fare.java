package wooteco.subway.domain.fare;

public class Fare {

    private final int value;

    public Fare(final int distance, final int age, final int extraFare) {
        this.value = calculate(distance, age, extraFare);
    }

    private int calculate(final int distance, final int age, final int extraFare) {
        final int fare = BasicFare.calculate(distance);
        return FareAgeDiscount.calculate(fare, age) + extraFare;
    }

    public int getValue() {
        return value;
    }
}
