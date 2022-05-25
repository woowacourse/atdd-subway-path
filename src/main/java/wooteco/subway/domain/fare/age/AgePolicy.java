package wooteco.subway.domain.fare.age;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgePolicy {

    NONE(AgePolicy::isNone, fare -> 0),
    CHILDREN(AgePolicy::isChildren, AgePolicy::calculateChildrenFare),
    TEENAGER(AgePolicy::isTeenager, AgePolicy::calculateTeenagerFare),
    ADULT(AgePolicy::isAdult, fare -> fare),
    ;

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    AgePolicy(
        final Predicate<Integer> predicate,
        final Function<Integer, Integer> function
    ) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int discount(final int age, final int fare) {
        return Arrays.stream(AgePolicy.values())
            .filter(agePolicy -> agePolicy.predicate.test(age))
            .map(agePolicy -> agePolicy.function.apply(fare))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
    }

    private static boolean isNone(final int age) {
        return age < 6;
    }

    private static boolean isChildren(final int age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeenager(final int age) {
        return age >= 13 && age < 19;
    }

    private static boolean isAdult(final int age) {
        return age >= 19;
    }

    private static int calculateChildrenFare(final int fare) {
        return (fare - 350) * 5 / 10;
    }

    private static int calculateTeenagerFare(final int fare) {
        return (fare - 350) * 8 / 10;
    }
}
