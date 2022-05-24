package wooteco.subway.domain.fare.discountstrategy;

public class ChildDiscountStrategy implements DiscountStrategy {

    private static final int DEDUCTION_FARE = 350;

    @Override
    public int getDiscountedFare(int fare) {
        return (fare - DEDUCTION_FARE) * 50 / 100;
    }
}
