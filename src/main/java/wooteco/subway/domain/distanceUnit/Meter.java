package wooteco.subway.domain.distanceUnit;

public class Meter extends DistanceUnit {

    public Meter(double value) {
        super(value);
    }

    @Override
    public Kilometer toKm() {
        return new Kilometer(value() / 1000);
    }
}
