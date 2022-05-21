package wooteco.subway.domain.distance;

public class Kilometer {

    private final int value;

    private Kilometer(int value) {
        this.value = value;
    }

    public static Kilometer from(int value) {
        return new Kilometer(value);
    }

    public int value() {
        return value;
    }

    public boolean lessThanKm(int otherValue) {
        return value <= otherValue;
    }

    public boolean moreThanKm(int otherValue) {
        return value >= otherValue;
    }

    public boolean exceedKm(int otherValue) {
        return value > otherValue;
    }
}
