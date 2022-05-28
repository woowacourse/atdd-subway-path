package wooteco.subway.domain.fare.discountpolicy;

class AdultDiscountPolicyStrategy implements DiscountPolicyStrategy {

    @Override
    public int calculateDiscountedFare(int fare) {
        return fare;
    }
}
