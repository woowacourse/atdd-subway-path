package wooteco.subway.domain.farepolicy;

public class BasicRangePolicy implements FarePolicy {

    @Override
    public int calculate(int distance) {
        return BASIC_FARE;
    }
}
