package wooteco.subway.domain.Fare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.distance.DistanceUnit;

public enum FarePolicy {

    LESS_THAN_10KM(d -> d.lessThanKm(10)) {
        @Override
        public Fare apply(DistanceUnit distanceUnit) {
            return MINIMUM_FARE;
        }
    },
    FROM_10KM_TO_50KM(d -> d.moreThanKm(10) && d.lessThanKm(50)) {
        @Override
        public Fare apply(DistanceUnit distanceUnit) {
            int distance = distanceUnit.toKm().value() - 10;
            return MINIMUM_FARE.add((int) ((Math.ceil((distance - 1) / 5) + 1) * 100));
        }
    },
    EXCEED_50KM(d -> d.exceedKm(50)) {
        @Override
        public Fare apply(DistanceUnit distanceUnit) {
            int distance = distanceUnit.toKm().value() - 50;
            return MINIMUM_FARE.add(800).add((int) ((Math.ceil((distance - 1) / 8) + 1) * 100));
        }
    };

    private static final Fare MINIMUM_FARE = new Fare(1250);

    private Predicate<DistanceUnit> condition;

    FarePolicy(Predicate<DistanceUnit> condition) {
        this.condition = condition;
    }

    abstract public Fare apply(DistanceUnit distanceUnit);

    public static Fare getFare(DistanceUnit distanceUnit) {
        FarePolicy farePolicy = findFarePolicy(distanceUnit);
        return farePolicy.apply(distanceUnit);
    }

    private static FarePolicy findFarePolicy(DistanceUnit distanceUnit) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.condition.test(distanceUnit))
                .findAny().orElseThrow(() -> new IllegalArgumentException("요금 정책을 찾을 수 없습니다."));
    }
}
