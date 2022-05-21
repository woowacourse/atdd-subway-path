package wooteco.subway.domain.Fare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.distance.Kilometer;

public enum FarePolicy {

    LESS_THAN_10KM(d -> d.lessThanKm(10)) {
        @Override
        public Fare apply(Kilometer kilometer) {
            return MINIMUM_FARE;
        }
    },
    FROM_10KM_TO_50KM(d -> d.moreThanKm(10) && d.lessThanKm(50)) {
        @Override
        public Fare apply(Kilometer kilometer) {
            int distance = kilometer.value() - 10;
            return MINIMUM_FARE.add((int) ((Math.ceil((distance - 1) / 5) + 1) * 100));
        }
    },
    EXCEED_50KM(d -> d.exceedKm(50)) {
        @Override
        public Fare apply(Kilometer kilometer) {
            int distance = kilometer.value() - 50;
            return MINIMUM_FARE.add(FROM_10KM_TO_50KM_FARE_VALUE)
                    .add((int) ((Math.ceil((distance - 1) / 8) + 1) * 100));
        }
    };

    private static final Fare MINIMUM_FARE = new Fare(1250);
    private static final int FROM_10KM_TO_50KM_FARE_VALUE = 800;

    private final Predicate<Kilometer> condition;

    FarePolicy(Predicate<Kilometer> condition) {
        this.condition = condition;
    }

    abstract public Fare apply(Kilometer kilometer);

    public static Fare getFare(Kilometer kilometer) {
        FarePolicy farePolicy = findFarePolicy(kilometer);
        return farePolicy.apply(kilometer);
    }

    private static FarePolicy findFarePolicy(Kilometer kilometer) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.condition.test(kilometer))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요금 정책을 찾을 수 없습니다."));
    }
}
