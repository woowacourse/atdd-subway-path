package wooteco.subway.domain.fare.discountstrategy;

import wooteco.subway.domain.fare.Fare;

public class TeenagerDiscountStrategy implements DiscountStrategy {
    private static final int DEDUCTING_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public Fare discount(Fare fare) {
        Fare deducted = fare.subtract(DEDUCTING_AMOUNT);
        return deducted.multiply(1 - DISCOUNT_RATE);
    }
}
