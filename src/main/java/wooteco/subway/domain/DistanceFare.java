package wooteco.subway.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceFare {
    BASIC_DISTANCE((distance) -> distance < 10, (distance) -> Constants.BASIC_FARE),

    ADDITIONAL_DISTANCE((distance) -> distance < 50,
            (distance) -> Constants.BASIC_FARE + (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100)),

    OTHER(ignore -> true,
            (distance) -> Constants.BASIC_FARE + Constants.ADDITIONAL_BASIC_FARE +
                    (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100)),
    ;

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    DistanceFare(final Predicate<Integer> predicate, final Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int valueOf(int price, int distance) {
        return Arrays.stream(values())
                .filter(fare -> fare.predicate.test(distance))
                .map(fare -> price + fare.function.apply(distance))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 거리에 따른 금액을 찾을수 없습니다."));
    }

    private static class Constants {
        private static final int BASIC_FARE = 1250;
        private static final int ADDITIONAL_BASIC_FARE = 800;
    }
}
