package wooteco.subway.domain.fare.strategy.discount;

public interface DiscountStrategy {

    int getDiscountedFare(int fare);
}
