package wooteco.subway.domain.strategy.discount;

import static wooteco.subway.domain.strategy.discount.KidDiscountStrategy.DEDUCTION;

public class AdolescentDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        int targetFare = fare - DEDUCTION;
        return targetFare - (int) (targetFare * 0.2);
    }
}
