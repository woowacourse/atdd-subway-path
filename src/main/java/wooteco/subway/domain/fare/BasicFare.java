package wooteco.subway.domain.fare;

public final class BasicFare implements Fare {

    private static final int VALUE = 1250;

    @Override
    public int calculate() {
        return VALUE;
    }
}
