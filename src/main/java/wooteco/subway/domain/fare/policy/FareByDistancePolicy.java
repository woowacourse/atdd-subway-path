package wooteco.subway.domain.fare.policy;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.section.Distance;

public enum FareByDistancePolicy {
    FIRST_SECTION(FareByDistancePolicy::isFirstSection, FareByDistancePolicy::calculateFirstSection),
    SECOND_SECTION(FareByDistancePolicy::isSecondSection, FareByDistancePolicy::calculateSecondSection),
    THIRD_SECTION(FareByDistancePolicy::isThirdSection, FareByDistancePolicy::calculateThirdSection),
    ;

    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_FARE_UNIT = 100;
    private static final int MAX_FIRST_OVER_FARE = 800;

    private static final int START_OF_FIRST_SECTION = 0;
    private static final int END_OF_FIRST_SECTION = 10;
    private static final int START_OF_SECOND_SECTION = 11;
    private static final int END_OF_SECOND_SECTION = 50;
    private static final int START_OF_THIRD_SECTION = 51;

    private static final int UNIT_DISTANCE_OF_SECOND_SECTION = 5;
    private static final int UNIT_DISTANCE_OF_THIRD_SECTION = 8;

    private final Predicate<Distance> distancePredicate;
    private final Function<Distance, Fare> calculate;

    FareByDistancePolicy(Predicate<Distance> distancePredicate, Function<Distance, Fare> calculate) {
        this.distancePredicate = distancePredicate;
        this.calculate = calculate;
    }

    public static FareByDistancePolicy from(Distance distance) {
        return Arrays.stream(values())
                .filter(fareByDistancePolicy -> fareByDistancePolicy.distancePredicate.test(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("거리에 해당하는 요금 정책을 찾을 수 없습니다."));
    }

    private static boolean isFirstSection(Distance distance) {
        return distance.isBetween(START_OF_FIRST_SECTION, END_OF_FIRST_SECTION);
    }

    private static boolean isSecondSection(Distance distance) {
        return distance.isBetween(START_OF_SECOND_SECTION, END_OF_SECOND_SECTION);
    }

    private static boolean isThirdSection(Distance distance) {
        return distance.isGreaterThanOrEqualTo(START_OF_THIRD_SECTION);
    }

    private static Fare calculateFirstSection(Distance distance) {
        return new Fare(DEFAULT_FARE);
    }

    private static Fare calculateSecondSection(Distance distance) {
        int overFare = calculateOverFare(distance.getValue() - END_OF_FIRST_SECTION,
                UNIT_DISTANCE_OF_SECOND_SECTION);
        return new Fare(DEFAULT_FARE + overFare);
    }

    private static Fare calculateThirdSection(Distance distance) {
        int overFare = calculateOverFare(distance.getValue() - END_OF_SECOND_SECTION,
                UNIT_DISTANCE_OF_THIRD_SECTION);
        return new Fare(DEFAULT_FARE + MAX_FIRST_OVER_FARE + overFare);
    }

    private static int calculateOverFare(double totalDistance, double unitDistance) {
        int overFareCount = (int) Math.ceil(totalDistance / unitDistance);
        return overFareCount * OVER_FARE_UNIT;
    }

    public Fare calculate(Distance distance) {
        return calculate.apply(distance);
    }
}
