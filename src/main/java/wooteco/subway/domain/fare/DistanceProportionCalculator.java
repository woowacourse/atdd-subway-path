package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceProportionCalculator {

    DEFAULT_STANDARD(distance -> 0 <= distance && distance <= 10, ignored -> 1250),
    FIRST_STANDARD(distance -> 10 < distance && distance <= 50,
            distance -> 1250 + (int) ((Math.ceil((distance - 10 - 1) / 5) + 1) * 100)),
    SECOND_STANDARD(distance -> distance > 50,
            distance -> 2050 + (int) ((Math.ceil((distance - 50 - 1) / 8) + 1) * 100))
    ;

    private final Predicate<Integer> condition;
    private final Function<Integer, Integer> function;

    DistanceProportionCalculator(Predicate<Integer> condition, Function<Integer, Integer> function) {
        this.condition = condition;
        this.function = function;
    }

    public static DistanceProportionCalculator from(int distance) {
        return Arrays.stream(values())
                .filter(it -> it.condition.test(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(distance + "는 계산이 불가능한 거리입니다."));
    }

    public int calculateFare(int distance) {
        return function.apply(distance);
    }
}
