package wooteco.subway.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceExtraFare {
    BASIC_DISTANCE((distance) -> distance <= 10, (distance) -> 0),
    ADDITIONAL_DISTANCE((distance) -> distance < 50,
            (distance) -> (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100)),
    OTHER(ignore -> true, (distance) -> 800 + (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100)),
    ;

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    DistanceExtraFare(final Predicate<Integer> predicate, final Function<Integer, Integer> function) {
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
