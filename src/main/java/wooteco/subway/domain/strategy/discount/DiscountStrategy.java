package wooteco.subway.domain.strategy.discount;

public interface DiscountStrategy {

    int calculateDiscountedAmount(final int fare);
}
