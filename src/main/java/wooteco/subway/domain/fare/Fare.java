package wooteco.subway.domain.fare;

import java.util.Objects;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.fare.strategy.discount.DiscountStrategyMapper;
import wooteco.subway.domain.fare.strategy.extrafare.ExtraFareStrategyMapper;

public class Fare {

    private static final int BASIC_VALUE = 1250;

    private final int value;

    private Fare(final int value) {
        this.value = value;
    }

    public static Fare from(final int extraFare) {
        return new Fare(BASIC_VALUE + extraFare);
    }

    public Fare addExtraFareByDistance(final Distance distance) {
        final int extraFare = ExtraFareStrategyMapper.findStrategyBy(distance)
                .apply(distance);
        return new Fare(value + extraFare);
    }

    public Fare discountByAge(final int age) {
        final int discountedFare = DiscountStrategyMapper.findStrategyBy(age)
                .apply(value);
        return new Fare(discountedFare);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
