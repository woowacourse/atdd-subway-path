package wooteco.subway.domain.strategy.fare;

import wooteco.subway.domain.strategy.fare.age.FareAgeStrategyManager;
import wooteco.subway.domain.strategy.fare.distance.FareDistanceStrategyManager;

public class FarePolicy {

    private final FareDistanceStrategyManager fareDistanceStrategyManager;
    private final FareAgeStrategyManager fareAgeStrategyManager;

    public FarePolicy(FareDistanceStrategyManager fareDistanceStrategyManager, FareAgeStrategyManager fareAgeStrategyManager) {
        this.fareDistanceStrategyManager = fareDistanceStrategyManager;
        this.fareAgeStrategyManager = fareAgeStrategyManager;
    }

    public int getFare(int age, int distance, int extraFare) {
        int distanceFare = fareDistanceStrategyManager.calculateDistanceFare(distance) + extraFare;
        return distanceFare - fareAgeStrategyManager.calculateDiscountAgeFare(age, distanceFare);
    }
}
