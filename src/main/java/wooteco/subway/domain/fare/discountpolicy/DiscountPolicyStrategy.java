package wooteco.subway.domain.fare.discountpolicy;

interface DiscountPolicyStrategy {

    int calculateDiscountedFare(int fare);
}
