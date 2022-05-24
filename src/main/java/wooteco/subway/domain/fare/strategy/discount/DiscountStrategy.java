package wooteco.subway.domain.fare.strategy.discount;

public interface DiscountStrategy {

    int DEFAULT_DISCOUNT_AMOUNT = 350;
    int FREE = 0;

    int calculate(final int fare);
}
