package wooteco.subway.domain.fare;


import static wooteco.subway.domain.fare.FarePolicy.Constants.FIFTY_KILOMETER;
import static wooteco.subway.domain.fare.FarePolicy.Constants.FIRST_SECTION_BASE_FARE;
import static wooteco.subway.domain.fare.FarePolicy.Constants.NO_FARE;
import static wooteco.subway.domain.fare.FarePolicy.Constants.SECOND_SECTION_ADDITIONAL;
import static wooteco.subway.domain.fare.FarePolicy.Constants.SECOND_SECTION_ADDITIONAL_STANDARD;
import static wooteco.subway.domain.fare.FarePolicy.Constants.TEN_KILOMETER;
import static wooteco.subway.domain.fare.FarePolicy.Constants.THIRD_SECTION_ADDITIONAL;
import static wooteco.subway.domain.fare.FarePolicy.Constants.THIRD_SECTION_ADDITIONAL_STANDARD;
import static wooteco.subway.domain.fare.FarePolicy.Constants.THIRD_SECTION_BASE_FARE;
import static wooteco.subway.domain.fare.FarePolicy.Constants.ZERO;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.NotFoundFareSectionException;

public enum FarePolicy {

    ZERO_SECTION(isZeroOrNegative(), distance -> NO_FARE),
    FIRST_SECTION(isTenOrLessThanTen(), distance -> FIRST_SECTION_BASE_FARE),
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
            distance -= TEN_KILOMETER;
            return FIRST_SECTION_BASE_FARE +
                    Double.valueOf(
                            Math.ceil(distance / SECOND_SECTION_ADDITIONAL_STANDARD) * SECOND_SECTION_ADDITIONAL
                    ).longValue();
        };
    }

    private static Function<Long, Long> thirdSectionFareCalculator() {
        return distance -> {
            distance -= FIFTY_KILOMETER;
            return THIRD_SECTION_BASE_FARE +
                    Double.valueOf(
                            Math.ceil(distance / THIRD_SECTION_ADDITIONAL_STANDARD) * THIRD_SECTION_ADDITIONAL
                    ).longValue();
        };
    }

    private static Predicate<Long> isZeroOrNegative() {
        return distance -> distance <= ZERO;
    }

    private static Predicate<Long> isTenOrLessThanTen() {
        return distance -> ZERO < distance && distance <= TEN_KILOMETER;
    }

    private static Predicate<Long> isGreaterThanTenAndEvenOrLessThanFifty() {
        return distance -> TEN_KILOMETER < distance && distance <= FIFTY_KILOMETER;
    }

    private static Predicate<Long> isGreaterThanFifty() {
        return distance -> FIFTY_KILOMETER < distance;
    }

    static class Constants {
        static final long NO_FARE = 0L;
        static final long FIRST_SECTION_BASE_FARE = 1250L;
        static final double SECOND_SECTION_ADDITIONAL_STANDARD = 5.0;
        static final long SECOND_SECTION_ADDITIONAL = 100L;
        static final long THIRD_SECTION_BASE_FARE = 2050L;
        static final double THIRD_SECTION_ADDITIONAL_STANDARD = 8.0;
        static final long THIRD_SECTION_ADDITIONAL = 100L;
        static final int ZERO = 0;
        static final int TEN_KILOMETER = 10;
        static final int FIFTY_KILOMETER = 50;
    }
}
