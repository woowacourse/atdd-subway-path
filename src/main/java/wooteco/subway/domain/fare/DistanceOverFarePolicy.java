package wooteco.subway.domain.fare;

public enum DistanceOverFarePolicy {

    OVER_TEN(10, 50, 5),
    OVER_FIFTY(50, Integer.MAX_VALUE, 8),
    ;

    private static final int OVER_FARE_AMOUNT = 100;

    private final int startExclusive;
    private final int endInclusive;
    private final int limit;

    DistanceOverFarePolicy(int startExclusive, int endInclusive, int limit) {
        this.startExclusive = startExclusive;
        this.endInclusive = endInclusive;
        this.limit = limit;
    }

    public boolean isApplicableTo(int value) {
        return value > startExclusive;
    }

    public int toOverFare(int distance) {
        if (distance <= startExclusive) {
            return 0;
        }
        int overDistance = Math.min(distance, endInclusive) - startExclusive;
        return calculateOverFare(overDistance, limit);
    }

    private int calculateOverFare(int overDistance, int limit) {
        double overFareDigit = Math.ceil((overDistance - 1) / limit) + 1;
        return (int) (overFareDigit * OVER_FARE_AMOUNT);
    }
}
