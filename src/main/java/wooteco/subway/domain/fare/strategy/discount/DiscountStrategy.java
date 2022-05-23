package wooteco.subway.domain.fare.strategy.discount;

public interface DiscountStrategy {

    int calculate(final int fare);
}
