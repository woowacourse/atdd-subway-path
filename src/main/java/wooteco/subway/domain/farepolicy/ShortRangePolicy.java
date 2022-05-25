package wooteco.subway.domain.farepolicy;

public class ShortRangePolicy implements FarePolicy {

    @Override
    public int calculate(int distance) {
        double rate = Math.ceil((distance - SHORT_RANGE_THRESHOLD_DISTANCE) / SHORT_RANGE_DISTANCE_RATE);
        return BASIC_FARE + (int) (Math.min(rate, MAX_SHORT_RATE) * OVER_FARE);
    }
}
