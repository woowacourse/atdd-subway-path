package wooteco.subway.domain.fare;

public interface FareDiscountPolicy {
    int calculateDiscountAmount(int amount);
}
