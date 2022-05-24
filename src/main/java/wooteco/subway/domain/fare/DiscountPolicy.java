package wooteco.subway.domain.fare;

public interface DiscountPolicy {

	Fare apply(Fare fare);
}
