package wooteco.subway.domain.fare.farestrategy.discount;

import wooteco.subway.domain.fare.farestrategy.DiscountStrategy;

public class AdultStrategy implements DiscountStrategy {

    @Override
    public long calculate(long fare) {
        return fare;
    }
}
