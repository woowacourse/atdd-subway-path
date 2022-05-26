package wooteco.subway.domain.fare.farepolicy;

@FunctionalInterface
public interface FareCalculateStrategy {

    int calculateFare(double distance);
}
