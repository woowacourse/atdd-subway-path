package wooteco.subway.domain.fare;

public interface DiscountPolicy {

    int calculate(int fare);
}
