package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.distance.Kilometer;

public enum DistanceFarePolicy {

    LESS_THAN_10KM(d -> d.lessThanKm(10)) {
        public Fare getFare(Fare fare, Kilometer kilometer) {
            return fare;
        }
    },
    FROM_10KM_TO_50KM(d -> d.moreThanKm(10) && d.lessThanKm(50)) {
        public Fare getFare(Fare fare, Kilometer kilometer) {
            int distance = kilometer.value() - 10;
            return fare.add((int) ((Math.ceil((distance - 1) / 5) + 1) * 100));
        }
    },
    EXCEED_50KM(d -> d.exceedKm(50)) {
        public Fare getFare(Fare fare, Kilometer kilometer) {
            int distance = kilometer.value() - 50;
            return fare.add(FROM_10KM_TO_50KM_FARE_VALUE)
                    .add((int) ((Math.ceil((distance - 1) / 8) + 1) * 100));
        }
    };

    private static final int FROM_10KM_TO_50KM_FARE_VALUE = 800;

    private final Predicate<Kilometer> condition;

    DistanceFarePolicy(Predicate<Kilometer> condition) {
        this.condition = condition;
    }

    abstract public Fare getFare(Fare fare, Kilometer kilometer);

    public static Fare apply(Fare fare, Kilometer kilometer) {
        DistanceFarePolicy farePolicy = findFarePolicy(kilometer);
        return farePolicy.getFare(fare, kilometer);
    }

    private static DistanceFarePolicy findFarePolicy(Kilometer kilometer) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.condition.test(kilometer))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요금 정책을 찾을 수 없습니다."));
    }
}
