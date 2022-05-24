package wooteco.subway.domain.fare.ageStrategy;

import wooteco.subway.exception.LowFareException;

public class TeenagerDiscountPolicy implements AgeDiscountPolicy {
    private static final int DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.8;

    @Override
    public int calculateFare(int fare) {
        if (fare < DISCOUNT_AMOUNT) {
            throw new LowFareException(DISCOUNT_AMOUNT + "원 미만의 금액은 할인할 수 없습니다.");
        }

        return (int)((fare - DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
