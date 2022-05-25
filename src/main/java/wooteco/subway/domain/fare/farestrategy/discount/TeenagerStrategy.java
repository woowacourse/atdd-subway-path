package wooteco.subway.domain.fare.farestrategy.discount;

import wooteco.subway.domain.fare.farestrategy.DiscountStrategy;

public class TeenagerStrategy implements DiscountStrategy {

    @Override
    public long calculate(long fare) {
        return (long) ((fare - 350) * 0.8);
    }
}
