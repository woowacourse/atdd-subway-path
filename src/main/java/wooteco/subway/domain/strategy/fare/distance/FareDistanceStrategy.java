package wooteco.subway.domain.strategy.fare.distance;

public interface FareDistanceStrategy {

    boolean isApplied(int distance);

    int distanceFare(int distance);
}
