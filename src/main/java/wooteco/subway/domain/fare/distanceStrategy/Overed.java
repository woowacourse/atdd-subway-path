package wooteco.subway.domain.fare.distanceStrategy;

public abstract class Overed {
    private static final int SURCHARGE = 100;

    public int calculateOverFare(int overDistance, int policy) {
        return (int)((Math.ceil((double)((overDistance - 1) / policy) + 1)) * SURCHARGE);
    }
}
