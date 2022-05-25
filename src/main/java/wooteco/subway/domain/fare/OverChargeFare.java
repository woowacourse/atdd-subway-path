package wooteco.subway.domain.fare;

public abstract class OverChargeFare implements Fare {

    private static final int PER_UNIT_FEE_AMOUNT = 100;

    protected int calculateOverFare(int distance, int minimumDistance, int unit) {
        return (int) ((Math.ceil((distance - minimumDistance - 1) / unit) + 1) * PER_UNIT_FEE_AMOUNT);
    }
}
