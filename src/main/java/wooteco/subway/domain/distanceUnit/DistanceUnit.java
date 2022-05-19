package wooteco.subway.domain.distanceUnit;

public abstract class DistanceUnit {

    private final double value;

    public DistanceUnit(double value) {
        this.value = value;
    }

    abstract public Kilometer toKm();

    public double value() {
        return value;
    }
}
