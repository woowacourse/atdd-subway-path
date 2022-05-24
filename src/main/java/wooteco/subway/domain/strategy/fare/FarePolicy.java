package wooteco.subway.domain.strategy.fare;

import wooteco.subway.domain.strategy.fare.age.FareDiscountAgeStrategy;
import wooteco.subway.domain.strategy.fare.distance.FareDistanceStrategy;

public class FarePolicy {

    private final FareDistanceStrategy fareDistanceStrategy;
    private final FareDiscountAgeStrategy fareAgeStrategyFactory;

    public FarePolicy(FareDistanceStrategy fareDistanceStrategy, FareDiscountAgeStrategy fareAgeStrategyFactory) {
        this.fareDistanceStrategy = fareDistanceStrategy;
        this.fareAgeStrategyFactory = fareAgeStrategyFactory;
    }

    public int getFare(int distance, int extraFare) {
        int distanceFare = fareDistanceStrategy.distanceFare(distance) + extraFare;
        return distanceFare - fareAgeStrategyFactory.discountAge(distanceFare);
    }
}
