package wooteco.subway.domain.fare.farestrategy.discount;

import wooteco.subway.domain.fare.farestrategy.DiscountStrategy;

public class ChildStrategy implements DiscountStrategy {
    @Override
    public long calculate(long fare) {
        return (long) ((fare - 350) * 0.5);
    }
}
