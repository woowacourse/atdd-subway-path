package wooteco.subway.domain.fare.ageStrategy;

public interface AgeDiscountPolicy {
    int calculateFare(int fare);
}
