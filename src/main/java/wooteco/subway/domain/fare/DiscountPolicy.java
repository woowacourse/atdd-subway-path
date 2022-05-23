package wooteco.subway.domain.fare;

public interface DiscountPolicy {

    Fare discountFare(Fare fare, int age);
}
