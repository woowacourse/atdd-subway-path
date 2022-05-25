package wooteco.subway.domain.fare.farestrategy.discount;

import wooteco.subway.domain.fare.farestrategy.DiscountStrategy;

public class InfantStrategy implements DiscountStrategy {

    @Override
    public long calculate(long fare) {
        return 0;
    }
}
