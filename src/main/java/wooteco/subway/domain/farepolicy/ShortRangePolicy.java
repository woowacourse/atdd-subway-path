package wooteco.subway.domain.farepolicy;

public class ShortRangePolicy implements FarePolicy {

    @Override
    public int calculate(int distance) {
        int distanceTraveled = distance - DistanceRange.BASIC_DISTANCE.maxDistance();
        double rate = Math.ceil(distanceTraveled / SHORT_RANGE_DISTANCE_RATE);
        return BASIC_FARE + (int) (Math.min(rate, MAX_SHORT_RATE) * OVER_FARE);
    }
}
