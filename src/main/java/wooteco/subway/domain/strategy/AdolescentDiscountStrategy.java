package wooteco.subway.domain.strategy;

import static wooteco.subway.domain.strategy.KidDiscountStrategy.DEDUCTION;

public class AdolescentDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        int targetFare = fare - DEDUCTION;
        return fare - (int) (targetFare * 0.2);
    }
}
