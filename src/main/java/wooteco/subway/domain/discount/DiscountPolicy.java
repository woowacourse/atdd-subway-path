package wooteco.subway.domain.discount;

import wooteco.subway.domain.discount.DiscountCondition;

public interface DiscountPolicy {

    boolean accept(final DiscountCondition discountCondition);

    int applyDiscount(final int money);
}
