package wooteco.subway.domain.strategy.fare;

import java.util.List;

public abstract class FarePolicy {

    public int calculateFare(int distance, List<Integer> extraPrices, int age) {
        int fare = calculateBasicFare(distance);
        fare += calculateExtraFare(extraPrices);
        fare -= calculateDiscountFare(fare, age);
        return fare;
    }

    protected abstract int calculateDiscountFare(int price, int age);

    protected abstract int calculateExtraFare(List<Integer> extraPrices);

    protected abstract int calculateBasicFare(int distance);
}
