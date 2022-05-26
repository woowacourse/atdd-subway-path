package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceFarePolicy {
    BASIC_DISTANCE(DistanceFarePolicy::isBasicDistance, (distance) -> 0),
    ADDITIONAL_DISTANCE(DistanceFarePolicy::isAdditionalDistance, DistanceFarePolicy::getAdditionalDistanceExtraFare),
    OTHER(DistanceFarePolicy::isLongerDistance, DistanceFarePolicy::getLongerDistanceExtraFare),
    ;

    private static final int BASIC_ADDITIONAL_FARE = 800;
    private static final int BASIC_DISTANCE_UPPER_BOUND = 10;
    private static final int BASIC_DISTANCE_LOWER_BOUND = 0;
    private static final int BASIC_ADDITIONAL_DISTANCE_UPPER_BOUND = 50;
    private static final int BASIC_ADDITIONAL_DISTANCE_LOWER_BOUND = 10;
    private static final int LONGER_DISTANCE_LOWER_BOUND = 50;

    private static int getLongerDistanceExtraFare(final Integer distance) {
        return BASIC_ADDITIONAL_FARE + (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100);
    }

    private static int getAdditionalDistanceExtraFare(final Integer distance) {
        return (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100);
    }

    private static boolean isLongerDistance(final Integer distance) {
        return LONGER_DISTANCE_LOWER_BOUND <= distance;
    }

    private static boolean isAdditionalDistance(final Integer distance) {
        return BASIC_ADDITIONAL_DISTANCE_LOWER_BOUND < distance && distance < BASIC_ADDITIONAL_DISTANCE_UPPER_BOUND;
    }

    private static boolean isBasicDistance(final Integer distance) {
        return BASIC_DISTANCE_LOWER_BOUND < distance && distance <= BASIC_DISTANCE_UPPER_BOUND;
    }

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    DistanceFarePolicy(final Predicate<Integer> predicate, final Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int valueOf(int distance) {
        return Arrays.stream(values())
                .filter(fare -> fare.predicate.test(distance))
                .map(fare -> fare.function.apply(distance))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 거리에 따른 금액을 찾을수 없습니다."));
    }
}
