package wooteco.subway.domain.strategy.fare;

import wooteco.subway.domain.strategy.fare.distance.FareDistanceStrategy;

public class FarePolicy {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int BASIC_DISTANCE = 10;
    public static final int STEP_ONE_DISTANCE = 50;
    public static final double STEP_ONE_CHARGE_DISTANCE = 5.0;
    public static final double STEP_TWO_CHARGE_DISTANCE = 8.0;

    private final FareDistanceStrategy fareDistanceStrategy;

    public FarePolicy(FareDistanceStrategy fareDistanceStrategy) {
        this.fareDistanceStrategy = fareDistanceStrategy;
    }

    public int getFare(int distance, int extraFare) {
        return fareDistanceStrategy.distanceFare(distance) + extraFare;
    }
}
