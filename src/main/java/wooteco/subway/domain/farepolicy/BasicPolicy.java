package wooteco.subway.domain.farepolicy;

public class BasicPolicy implements FarePolicy {

    @Override
    public int calculate(int basicFare) {
        return basicFare;
    }
}
