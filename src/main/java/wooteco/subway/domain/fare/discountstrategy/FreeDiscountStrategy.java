package wooteco.subway.domain.fare.discountstrategy;

public class FreeDiscountStrategy implements DiscountStrategy {

    @Override
    public int getDiscountedFare(int fare) {
        return 0;
    }
}
