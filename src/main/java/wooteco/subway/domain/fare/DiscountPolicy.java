package wooteco.subway.domain.fare;

public interface DiscountPolicy {

	int apply(int fare);
}
