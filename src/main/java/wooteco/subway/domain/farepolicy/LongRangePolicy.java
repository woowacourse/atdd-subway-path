package wooteco.subway.domain.farepolicy;

public class LongRangePolicy implements FarePolicy {

    @Override
    public int calculate(int distance) {
        int distanceTraveled = distance - DistanceRange.SHORT_RANGE.maxDistance();
        double rate = Math.ceil(distanceTraveled / LONG_RANGE_DISTANCE_RATE);
        return BASIC_FARE + (int) (MAX_SHORT_RATE + rate) * OVER_FARE;
    }
}
