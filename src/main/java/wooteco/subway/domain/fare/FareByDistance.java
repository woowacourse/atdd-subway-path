package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.exception.PositiveDigitException;

public enum FareByDistance {

    BASIC_FARE((distance) -> distance > 0 && distance <= 10, (distance) -> 1250),
    FIRST_EXTRA_FARE((distance) -> distance > 10 && distance <= 50, FareByDistance::calculateFirstExtraFare),
    SECOND_EXTRA_FARE((distance) -> distance > 50, FareByDistance::calculateSecondExtraFare);

    private static final int FIRST_EXTRA_FARE_DISTANCE = 10;
    private static final int SECOND_EXTRA_FARE_DISTANCE = 50;
    private static final int FIRST_EXTRA_FARE_STANDARD = 5;
    private static final int SECOND_EXTRA_FARE_STANDARD = 8;
    private static final int MAX_FIRST_EXTRA_FARE = 2050;
    private static final int BASIC_EXTRA_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final Predicate<Double> predicate;
    private final Function<Double, Integer> function;

    FareByDistance(final Predicate<Double> predicate, final Function<Double, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }


    public static int findFare(final double distance) {
        return Arrays.stream(values())
                .filter(fareByDistance -> fareByDistance.predicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new PositiveDigitException("구간의 길이는 양수여야 합니다."))
                .function
                .apply(distance);
    }

    private static int calculateFirstExtraFare(final double distance) {
        return BASIC_EXTRA_FARE + addFirstExtraFare(distance - FIRST_EXTRA_FARE_DISTANCE);
    }

    private static int calculateSecondExtraFare(final double distance) {
        return MAX_FIRST_EXTRA_FARE + addSecondExtraFare(distance - SECOND_EXTRA_FARE_DISTANCE);
    }

    private static int addSecondExtraFare(final double distance) {
        return (int) ((Math.ceil((distance) / SECOND_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }

    private static int addFirstExtraFare(final double distance) {
        return (int) ((Math.ceil((distance) / FIRST_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }
}
