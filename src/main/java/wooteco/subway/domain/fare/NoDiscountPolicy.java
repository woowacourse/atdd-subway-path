package wooteco.subway.domain.fare;

public class NoDiscountPolicy implements FareDiscountPolicy {
    @Override
    public int calculateDiscountAmount(final int amount) {
        return 0;
    }
}
