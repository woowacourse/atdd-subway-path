package wooteco.subway.domain;

import wooteco.subway.domain.distanceUnit.DistanceUnit;
import wooteco.subway.domain.distanceUnit.Kilometer;

public class FareCalculator {
    private static final int MINIMUM_FARE = 1250;

    public int calculate(DistanceUnit distance) {
        Kilometer distanceKilometer = distance.toKm();
        return MINIMUM_FARE + calculateOver50km(distanceKilometer) + calculateOver10kmUnder50km(distanceKilometer);
    }

    private int calculateOver10kmUnder50km(Kilometer distanceKilometer) {
        double extraDistance = distanceKilometer.value() - 10;
        if (extraDistance <= 0 || extraDistance > 40) {
            return 0;
        }
        return (int) ((Math.ceil(extraDistance / 5)) * 100);
    }

    private int calculateOver50km(Kilometer distanceKilometer) {
        double extraDistance = distanceKilometer.value() - 50;
        if (extraDistance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil(extraDistance / 8)) * 100) + calculateOver10kmUnder50km(new Kilometer(50));
    }
}
