package wooteco.subway.domain.distance;

public class Kilometer extends DistanceUnit {
    public Kilometer(int value) {
        super(value);
    }

    @Override
    public Kilometer toKm() {
        return this;
    }

    @Override
    public String toString() {
        return value() + "km";
    }
}
