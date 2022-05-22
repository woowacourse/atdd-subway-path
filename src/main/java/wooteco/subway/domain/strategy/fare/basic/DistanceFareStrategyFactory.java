package wooteco.subway.domain.strategy.fare.basic;


import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.strategy.fare.discount.DiscountStrategy;

public enum DistanceFareStrategyFactory {
    BASIC(DistanceFareStrategyFactory::isBasic, new BasicDistanceFareStrategy()),
    INTERMEDIATE(DistanceFareStrategyFactory::isIntermediate, new IntermediateDistanceFareStrategy()),
    ADVANCED(DistanceFareStrategyFactory::isAdvanced, new AdvancedDistanceFareStrategy());

    private static final int BASIC_DISTANCE_MINIMUM = 0;
    private static final int BASIC_DISTANCE_MAXIMUM = 10;
    private static final int INTERMEDIATE_DISTANCE_MAXIMUM = 50;

    private final Predicate<Integer> predicate;
    private final DistanceFareStrategy fareDistanceStrategy;

    DistanceFareStrategyFactory(Predicate<Integer> predicate, DistanceFareStrategy fareDistanceStrategy) {
        this.predicate = predicate;
        this.fareDistanceStrategy = fareDistanceStrategy;
    }

    private static boolean isBasic(Integer distance) {
        return distance >= BASIC_DISTANCE_MINIMUM && distance <= BASIC_DISTANCE_MAXIMUM;
    }

    private static boolean isIntermediate(Integer distance) {
        return distance > BASIC_DISTANCE_MAXIMUM && distance <= INTERMEDIATE_DISTANCE_MAXIMUM;
    }

    private static boolean isAdvanced(Integer distance) {
        return distance > INTERMEDIATE_DISTANCE_MAXIMUM;
    }

    public static DistanceFareStrategy getDistanceFareStrategy(int distance) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(distance))
                .findFirst()
                .map(value -> value.fareDistanceStrategy)
                .orElseThrow(() -> new IllegalArgumentException("거리에 맞는 요금이 존재하지 않습니다"));
    }
}
