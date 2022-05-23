package wooteco.subway.domain.fare;

public abstract class Fare {

    protected static final int DEFAULT_FARE_AMOUNT = 1250;
    protected static final long PER_UNIT_FEE_AMOUNT = 100L;
    protected static final int MAXIMUM_DEFAULT_DISTANCE = 10;
    protected static final int MAXIMUM_EXTRA_DISTANCE = 50;
    protected static final int DEFAULT_UNIT = 5;
    protected static final int EXTRA_UNIT = 8;

    protected int distance;
    protected int extraFare;

    public Fare(int distance, int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public abstract int calculateFare();

    protected int calculateOverFare(int distance, int perUnit) {
        return (int) ((Math.ceil((distance - 1) / perUnit) + 1) * PER_UNIT_FEE_AMOUNT);
    }
}
