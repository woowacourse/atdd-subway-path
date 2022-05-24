package wooteco.subway.domain.strategy.fare.distance;


import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceFareStrategyFactory {
    FIRST(DistanceFareStrategyFactory::isFirst, new FirstDistanceFareStrategy()),
    SECOND(DistanceFareStrategyFactory::isSecond, new SecondDistanceFareStrategy()),
    THIRD(DistanceFareStrategyFactory::isAdvanced, new ThirdDistanceFareStrategy());

    private static final int FIRST_DISTANCE_MINIMUM = 0;
    private static final int FIRST_DISTANCE_MAXIMUM = 10;
    private static final int SECOND_DISTANCE_MAXIMUM = 50;

    private final Predicate<Integer> predicate;
    private final DistanceFareStrategy fareDistanceStrategy;

    DistanceFareStrategyFactory(Predicate<Integer> predicate, DistanceFareStrategy fareDistanceStrategy) {
        this.predicate = predicate;
        this.fareDistanceStrategy = fareDistanceStrategy;
    }

    private static boolean isFirst(Integer distance) {
        return FIRST_DISTANCE_MINIMUM <= distance && distance <= FIRST_DISTANCE_MAXIMUM;
    }

    private static boolean isSecond(Integer distance) {
        return FIRST_DISTANCE_MAXIMUM < distance && distance <= SECOND_DISTANCE_MAXIMUM;
    }

    private static boolean isAdvanced(Integer distance) {
        return distance > SECOND_DISTANCE_MAXIMUM;
    }

    public static DistanceFareStrategy getDistanceFareStrategy(int distance) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(distance))
                .findFirst()
                .map(value -> value.fareDistanceStrategy)
                .orElseThrow(() -> new IllegalArgumentException("거리에 맞는 요금이 존재하지 않습니다"));
    }
}
