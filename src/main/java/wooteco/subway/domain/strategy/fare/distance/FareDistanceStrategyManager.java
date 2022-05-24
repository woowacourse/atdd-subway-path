package wooteco.subway.domain.strategy.fare.distance;

import java.util.List;
import wooteco.subway.exception.FareDistanceStrategyNotFoundException;

public class FareDistanceStrategyManager {

    private final List<FareDistanceStrategy> fareDistanceStrategies;

    public FareDistanceStrategyManager(List<FareDistanceStrategy> fareDistanceStrategies) {
        this.fareDistanceStrategies = fareDistanceStrategies;
    }

    public int calculateDistanceFare(int distance) {
        validateDistance(distance);
        return fareDistanceStrategies.stream()
                .filter(it -> it.isApplied(distance))
                .mapToInt(it -> it.distanceFare(distance))
                .sum();
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new FareDistanceStrategyNotFoundException();
        }
    }
}
