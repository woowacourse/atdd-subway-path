package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.NotFoundFareSectionException;

public enum FarePolicy {

    ZERO_SECTION(isEvenOrLessThanTen(), distance -> 0L),
    FIRST_SECTION(isLessThanTen(), distance -> 1250L),
    SECOND_SECTION(isGreaterThanTenAndEvenOrLessThanFifty(), secondSectionFareCalculator()),
    THIRD_SECTION(isGreaterThanFifty(), thirdSectionFareCalculator());

    private Predicate<Long> fareSectionFinder;
    private Function<Long, Long> fareCalculator;

    FarePolicy(Predicate<Long> fareSectionFinder, Function<Long, Long> fareCalculator) {
        this.fareSectionFinder = fareSectionFinder;
        this.fareCalculator = fareCalculator;
    }

    public static long calculateByDistance(long distance, long extraFare) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.fareSectionFinder.test(distance))
                .findAny()
                .orElseThrow(() -> new NotFoundFareSectionException(distance))
                .fareCalculator
                .apply(distance) + extraFare;
    }

    private static Function<Long, Long> secondSectionFareCalculator() {
        return distance -> {
            distance -= 10;
            return 1250L + Double.valueOf(Math.ceil(distance / 5.0) * 100L).longValue();
        };
    }

    private static Function<Long, Long> thirdSectionFareCalculator() {
        return distance -> {
            distance -= 50;
            return 2050L + Double.valueOf(Math.ceil(distance / 8.0) * 100L).longValue();
        };
    }

    private static Predicate<Long> isEvenOrLessThanTen() {
        return distance -> distance <= 0;
    }

    private static Predicate<Long> isLessThanTen() {
        return distance -> 0 < distance && distance <= 10;
    }

    private static Predicate<Long> isGreaterThanTenAndEvenOrLessThanFifty() {
        return distance -> 10 < distance && distance <= 50;
    }

    private static Predicate<Long> isGreaterThanFifty() {
        return distance -> 50 < distance;
    }
}
