package wooteco.subway.domain.distance;

public abstract class DistanceUnit {

    private final int value;

    public DistanceUnit(int value) {
        this.value = value;
    }

    abstract public Kilometer toKm();

    public int value() {
        return value;
    }

    public boolean moreThanKm(int distanceValue) {
        return value >= distanceValue;
    }

    public boolean lessThanKm(int distanceValue) {
        return value <= distanceValue;
    }

    public boolean exceedKm(int distanceValue) {
        return value > distanceValue;
    }
}
