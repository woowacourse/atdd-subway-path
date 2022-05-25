package wooteco.subway.domain.fare;

import wooteco.subway.domain.fare.farestrategy.ChargeStrategy;
import wooteco.subway.domain.fare.farestrategy.DiscountStrategy;

public class Fare {

    private final Long fareAmount;

    public Fare(Long fareAmount) {
        this.fareAmount = fareAmount;
    }

    public Fare chargeOf(ChargeStrategy chargeStrategy) {
        long fareAmount = chargeStrategy.calculate(this.fareAmount);
        return new Fare(fareAmount);
    }

    public Fare discountOf(DiscountStrategy discountStrategy) {
        long fareAmount = discountStrategy.calculate(this.fareAmount);
        return new Fare(fareAmount);
    }

    public Long getValue() {
        return fareAmount;
    }
}
