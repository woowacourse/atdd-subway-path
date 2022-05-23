package wooteco.subway.domain;

import java.util.Optional;

public enum FareStandard {
    DEFAULT(0, 10, 1, 0, 1250),
    SECOND(10, 50, 5, 100, 0),
    THIRD(50, Integer.MAX_VALUE, 8, 100, 0);

    private int lowerDistance;
    private int upperDistance;
    private int perKm;
    private int perFare;
    private int basicFare;

    public int calculate(double distance) {
        if (distance - lowerDistance < 0) {
            return 0;
        }
        if (distance - upperDistance > 0) {
            return (int) (Math.ceil(((double)upperDistance - lowerDistance) / perKm) * perFare) + basicFare;
        }
        return (int) (Math.ceil((distance - lowerDistance) / perKm) * perFare) + basicFare;
    }

    public Optional<FareStandard> update() {
        if (this.equals(DEFAULT)) {
            return Optional.of(SECOND);
        }
        if (this.equals(SECOND)) {
            return Optional.of(THIRD);
        }
        return Optional.empty();
    }

    FareStandard(int lowerDistance, int upperDistance, int perKm, int perFare, int basicFare) {
        this.lowerDistance = lowerDistance;
        this.upperDistance = upperDistance;
        this.perKm = perKm;
        this.perFare = perFare;
        this.basicFare = basicFare;
    }
}
