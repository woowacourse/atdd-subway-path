package wooteco.subway.domain.farepolicy;

public class PreferentialPolicy implements FarePolicy {

    @Override
    public int calculate(int basicFare) {
        return 0;
    }
}
