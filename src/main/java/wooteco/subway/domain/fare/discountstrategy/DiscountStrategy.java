package wooteco.subway.domain.fare.discountstrategy;

public interface DiscountStrategy {

    int getDiscountedFare(int fare);
}
