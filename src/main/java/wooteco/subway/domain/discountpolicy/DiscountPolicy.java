package wooteco.subway.domain.discountpolicy;

@FunctionalInterface
public interface DiscountPolicy {

    int DEDUCTED_AMOUNT = 350;

    int calculate(int basicFare);
}
