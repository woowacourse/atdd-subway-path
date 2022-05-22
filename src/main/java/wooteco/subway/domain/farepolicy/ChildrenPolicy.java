package wooteco.subway.domain.farepolicy;

public class ChildrenPolicy implements FarePolicy {

    @Override
    public int calculate(int basicFare) {
        return (int) ((basicFare - 350) * 0.5);
    }
}
