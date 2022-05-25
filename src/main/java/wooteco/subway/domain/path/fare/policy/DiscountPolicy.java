package wooteco.subway.domain.path.fare.policy;

public interface DiscountPolicy {

    int calculate(int fare);
}
