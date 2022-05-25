package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FarePolicy {

    FREE(
            distance -> distance == 0,
            distance -> 0
    ),
    BASIC(
            distance -> distance > 0 && distance <= 10,
            distance -> 1250
    ),
    FIRST_ADD(
            distance -> distance > 10 && distance <= 50,
            distance -> BASIC.function.apply(10) + calculateOverFare(distance - 10, 5)
    ),
    SECOND_ADD(
            distance -> distance > 50,
            distance -> FIRST_ADD.function.apply(50) + calculateOverFare(distance - 50, 8)
    );

    private static final String NOT_EXIST_MATCHED_DISTANCE = "일치하는 거리가 없습니다.";
    private static final int ADDED_FARE = 100;

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    FarePolicy(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int calculate(int distance) {
        return Arrays.stream(values())
                .filter(policy -> policy.predicate.test(distance))
                .map(policy -> policy.function.apply(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MATCHED_DISTANCE));
    }

    private static int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil((distance - 1) / kilometer) + 1) * ADDED_FARE);
    }
}
