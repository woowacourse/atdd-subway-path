package wooteco.subway.domain.fare.strategy;

@FunctionalInterface
public interface FareCalculateStrategy {

    int calculateFare(double distance);
}
