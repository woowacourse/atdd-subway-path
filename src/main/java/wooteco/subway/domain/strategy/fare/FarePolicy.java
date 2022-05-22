package wooteco.subway.domain.strategy.fare;

import wooteco.subway.domain.strategy.fare.age.FareDiscountAgeStrategy;
import wooteco.subway.domain.strategy.fare.distance.FareDistanceStrategy;

public class FarePolicy {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int BASIC_DISTANCE = 10;
    public static final int STEP_ONE_DISTANCE = 50;
    public static final double STEP_ONE_CHARGE_DISTANCE = 5.0;
    public static final double STEP_TWO_CHARGE_DISTANCE = 8.0;

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
