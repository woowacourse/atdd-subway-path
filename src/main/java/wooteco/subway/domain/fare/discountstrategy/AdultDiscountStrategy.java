package wooteco.subway.domain.fare.discountstrategy;

public class AdultDiscountStrategy implements DiscountStrategy {

    @Override
    public int getDiscountedFare(int fare) {
        return fare;
    }
}
