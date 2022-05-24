package wooteco.subway.domain.discount;

public interface DiscountPolicy {

    boolean accept(final DiscountCondition discountCondition);

    int applyDiscount(final int money);
}
