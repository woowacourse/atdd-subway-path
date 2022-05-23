package wooteco.subway.domain.fare.ageStrategy;

import wooteco.subway.exception.LowFareException;

public class TeenagerDiscountPolicy implements AgeDiscountPolicy {
    private static final int DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.8;

    @Override
    public int calculateFare(int fare) {
        if (fare < DISCOUNT_AMOUNT) {
            throw new LowFareException("금액 계산중 오류가 발생했습니다.");
        }

        return (int)((fare - DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
