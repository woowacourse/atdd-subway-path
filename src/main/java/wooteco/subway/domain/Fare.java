package wooteco.subway.domain;

import java.util.Objects;

public class Fare {
    private static final int DISCOUNT_EXCEPT_MONEY_UNIT = 350;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(int distance, int extraFare, int age) {
        DistanceFare distanceFare = DistanceFare.from(distance);
        int defaultFare = distanceFare.calculateFare(distance);

        DiscountRatesAge discountRatesAge = DiscountRatesAge.from(age);
        double discountRate = discountRatesAge.getDiscountRate();

        int fare = defaultFare + extraFare;
        int discount = (int) ((fare - DISCOUNT_EXCEPT_MONEY_UNIT) * discountRate);

        return new Fare(fare - discount);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
