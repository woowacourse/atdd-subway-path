package wooteco.subway.domain.strategy.fare.discount;

public interface DiscountStrategy {
    int calculateDiscount(int price);
}
