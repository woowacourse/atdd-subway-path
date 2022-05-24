package wooteco.subway.domain.strategy.fare;

import wooteco.subway.domain.strategy.fare.age.FareAgeStrategyManager;
import wooteco.subway.domain.strategy.fare.distance.FareDistanceStrategyManager;

public class FarePolicy {

    private final FareDistanceStrategyManager fareDistanceStrategyManger;
    private final FareAgeStrategyManager fareAgeStrategyManager;

    public FarePolicy(FareDistanceStrategyManager fareDistanceStrategyManger, FareAgeStrategyManager fareAgeStrategyManager) {
        this.fareDistanceStrategyManger = fareDistanceStrategyManger;
        this.fareAgeStrategyManager = fareAgeStrategyManager;
    }

    public int getFare(int age, int distance, int extraFare) {
        int distanceFare = fareDistanceStrategyManger.calculateDistanceFare(distance) + extraFare;
        return distanceFare - fareAgeStrategyManager.calculateDiscountAgeFare(age, distanceFare);
    }
}
