package wooteco.subway.domain.path.strategy;

import static wooteco.subway.domain.path.strategy.KidDiscountStrategy.DEDUCTION;

public class AdolescentDiscountStrategy implements DiscountStrategy {

    @Override
    public int calculate(int fare) {
        int targetFare = fare - DEDUCTION;
        return targetFare - (int) (targetFare * 0.2);
    }
}
