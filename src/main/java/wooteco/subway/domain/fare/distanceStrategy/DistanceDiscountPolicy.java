package wooteco.subway.domain.fare.distanceStrategy;

public interface DistanceDiscountPolicy {
    int calculateFare(int distance, double fare);
}
