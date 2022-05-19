package wooteco.subway.domain.distanceUnit;

public class Kilometer extends DistanceUnit {
    public Kilometer(double value) {
        super(value);
    }

    @Override
    public Kilometer toKm() {
        return this;
    }
}
