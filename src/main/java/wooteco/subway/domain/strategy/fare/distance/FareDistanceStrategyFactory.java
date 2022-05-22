package wooteco.subway.domain.strategy.fare.distance;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.exception.FareDistanceStrategyNotFoundException;

public enum FareDistanceStrategyFactory {

    DEFAULT(FareDistanceStrategyFactory::isDefault, new DefaultStrategy()),
    OVER_10_KM(FareDistanceStrategyFactory::isOver10Km, new Over10KmStrategy()),
    OVER_50_KM(FareDistanceStrategyFactory::isOver50Km, new Over50KmStrategy());

    private static final int DEFAULT_DISTANCE_MINIMUM = 1;
    private static final int DEFAULT_DISTANCE_MAXIMUM = 10;
    private static final int OVER_STEP_ONE_DISTANCE = 50;

    private final Predicate<Integer> predicate;
    private final FareDistanceStrategy fareDistanceStrategy;

    FareDistanceStrategyFactory(Predicate<Integer> predicate, FareDistanceStrategy fareDistanceStrategy) {
        this.predicate = predicate;
        this.fareDistanceStrategy = fareDistanceStrategy;
    }

    private static boolean isDefault(Integer distance) {
        return distance >= DEFAULT_DISTANCE_MINIMUM && distance <= DEFAULT_DISTANCE_MAXIMUM;
    }

    private static boolean isOver10Km(Integer distance) {
        return distance > DEFAULT_DISTANCE_MAXIMUM && distance <= OVER_STEP_ONE_DISTANCE;
    }

    private static boolean isOver50Km(Integer distance) {
        return distance > OVER_STEP_ONE_DISTANCE;
    }

    public static FareDistanceStrategy getStrategy(int distance) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(distance))
                .findFirst()
                .orElseThrow(FareDistanceStrategyNotFoundException::new)
                .fareDistanceStrategy;
    }
}
