package wooteco.subway.domain.farepolicy;

public class TeenagerPolicy implements FarePolicy {

    @Override
    public int calculate(int basicFare) {
        return (int) ((basicFare - 350) * 0.8);
    }
}
