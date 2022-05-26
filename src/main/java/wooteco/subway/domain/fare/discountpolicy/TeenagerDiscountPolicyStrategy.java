package wooteco.subway.domain.fare.discountpolicy;

class TeenagerDiscountPolicyStrategy implements DiscountPolicyStrategy {

    private static final int DEDUCTION_FARE = 350;

    @Override
    public int calculateDiscountedFare(int fare) {
        return (fare - DEDUCTION_FARE) * 80 / 100;
    }
}
