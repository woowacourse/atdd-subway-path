package wooteco.subway.domain.property.fare;

import wooteco.subway.domain.property.Distance;

public class DistanceFarePolicy implements FarePolicy {

    private static final int LOW_DISTANCE_VALUE = 10;
    private static final int HIGH_DISTANCE_VALUE = 50;

    private static final Distance LOW_DISTANCE = new Distance(10);
    private static final Distance HIGH_DISTANCE = new Distance(50);

    private static final double LOW_DISTANCE_DENOMINATOR = 5;
    private static final double HIGH_DISTANCE_DENOMINATOR = 8;

    private static final int EXTRA_FARE_AMOUNT = 100;

    private final Distance distance;

    public DistanceFarePolicy(Distance distance) {
        this.distance = distance;
    }

    @Override
    public Fare apply(Fare fare) {
        return fare.surcharge(surchargeLowExtra())
            .surcharge(surchargeHighExtra());
    }

    private int surchargeLowExtra() {
        if (distance.isShorterOrEqualThan(LOW_DISTANCE)) {
            return 0;
        }
        final int lowExtraDistance = Math.min(distance.getValue() - LOW_DISTANCE_VALUE,
            HIGH_DISTANCE_VALUE - LOW_DISTANCE_VALUE);
        return calculateForExtraDistance(lowExtraDistance, LOW_DISTANCE_DENOMINATOR);
    }

    private int calculateForExtraDistance(int extraDistance, double denominator) {
        return (int)(Math.ceil(extraDistance / denominator) * EXTRA_FARE_AMOUNT);
    }

    private int surchargeHighExtra() {
        if (distance.isShorterOrEqualThan(HIGH_DISTANCE)) {
            return 0;
        }
        return calculateForExtraDistance(distance.getValue() - HIGH_DISTANCE_VALUE, HIGH_DISTANCE_DENOMINATOR);
    }
}

