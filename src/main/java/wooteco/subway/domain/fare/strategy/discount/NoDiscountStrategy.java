package wooteco.subway.domain.fare.strategy.discount;

public class NoDiscountStrategy implements DiscountStrategy {

    @Override
    public int getDiscountedFare(int fare) {
        return fare;
    }
}
