package wooteco.subway.domain.strategy.fare.distance;

import java.util.List;

public class DistanceFareManager {

    private final List<DistanceFareStrategy> distanceFareStrategies;

    public DistanceFareManager(List<DistanceFareStrategy> distanceFareStrategies) {
        this.distanceFareStrategies = distanceFareStrategies;
    }

    public int calculateFare(int distance) {
        int sum = 0;
        for (DistanceFareStrategy distanceFareStrategy : distanceFareStrategies) {
            if (distanceFareStrategy.isUsable(distance)) {
                sum += distanceFareStrategy.calculateFare(distance);
            }
        }
        return sum;
    }
}
