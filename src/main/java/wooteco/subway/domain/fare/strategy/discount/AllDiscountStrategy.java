package wooteco.subway.domain.fare.strategy.discount;

public class AllDiscountStrategy implements DiscountStrategy {

    @Override
    public int getDiscountedFare(int fare) {
        return 0;
    }
}
