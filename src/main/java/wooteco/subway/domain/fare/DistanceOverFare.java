package wooteco.subway.domain.fare;

import java.util.Arrays;

public final class DistanceOverFare extends FarePolicy {

    private final int distance;

    public DistanceOverFare(Fare delegate, int distance) {
        super(delegate);
        this.distance = distance;
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        int distanceOverFare = calculateOverFare();
        return fare + distanceOverFare;
    }

    private int calculateOverFare() {
        return Arrays.stream(DistanceOverFarePolicy.values())
                .filter(policy -> policy.isApplicableTo(distance))
                .mapToInt(policy -> policy.toOverFare(distance))
                .sum();
    }

    private enum DistanceOverFarePolicy {

        OVER_TEN(10, 50, 5),
        OVER_FIFTY(50, Integer.MAX_VALUE, 8),
        ;

        static final int OVER_FARE_AMOUNT = 100;

        final int startExclusive;
        final int endInclusive;
        final int limit;

        DistanceOverFarePolicy(int startExclusive, int endInclusive, int limit) {
            this.startExclusive = startExclusive;
            this.endInclusive = endInclusive;
            this.limit = limit;
        }

        boolean isApplicableTo(int value) {
            return value > startExclusive;
        }

        int toOverFare(int distance) {
            int overDistance = Math.min(distance, endInclusive) - startExclusive;
            return calculateOverFare(overDistance, limit);
        }

        int calculateOverFare(int overDistance, int limit) {
            double overFareDigit = Math.ceil((overDistance - 1) / limit) + 1;
            return (int) (overFareDigit * OVER_FARE_AMOUNT);
        }
    }
}
