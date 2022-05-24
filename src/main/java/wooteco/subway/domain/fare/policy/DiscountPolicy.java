package wooteco.subway.domain.fare.policy;

public interface DiscountPolicy {

    int calculate(int fare);
}
