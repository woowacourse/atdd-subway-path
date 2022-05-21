package wooteco.subway.domain.path.fare;

public class BasicFare implements Fare {

    private static final int VALUE = 1250;

    @Override
    public int calculate() {
        return VALUE;
    }
}
