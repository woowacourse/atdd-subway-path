package wooteco.subway.domain.farepolicy;

public class LongRangePolicy implements FarePolicy {

    @Override
    public int calculate(int distance) {
        double rate = (distance - LONG_RANGE_THRESHOLD_DISTANCE) / LONG_RANGE_DISTANCE_RATE;
        return BASIC_FARE + (int) Math.ceil(MAX_SHORT_RATE + rate) * OVER_FARE;
    }
}
