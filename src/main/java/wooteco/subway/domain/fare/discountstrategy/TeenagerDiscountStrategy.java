package wooteco.subway.domain.fare.discountstrategy;

public class TeenagerDiscountStrategy implements DiscountStrategy {

    private static final int DEDUCTION_FARE = 350;

    @Override
    public int getDiscountedFare(int fare) {
        return (fare - DEDUCTION_FARE) * 80 / 100;
    }
}
