package wooteco.subway.domain.fare.discountpolicy;

class FreeDiscountPolicyStrategy implements DiscountPolicyStrategy {

    @Override
    public int calculateDiscountedFare(int fare) {
        return 0;
    }
}
