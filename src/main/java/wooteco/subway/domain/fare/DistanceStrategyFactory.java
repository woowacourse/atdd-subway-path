package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.distancestrategy.BasicDistanceStrategy;
import wooteco.subway.domain.fare.distancestrategy.DistanceStrategy;
import wooteco.subway.domain.fare.distancestrategy.FirstDistanceStrategy;
import wooteco.subway.domain.fare.distancestrategy.SecondDistanceStrategy;

public enum DistanceStrategyFactory {

    BASIC(distance -> distance <= 10, new BasicDistanceStrategy()),
    FIRST(distance -> distance > 10 && distance <= 50, new FirstDistanceStrategy()),
    SECOND(distance -> distance > 50, new SecondDistanceStrategy());

    private static final String NOT_EXIST_MATCHED_DISTANCE = "일치하는 거리가 없습니다.";

    private final Predicate<Integer> predicate;
    private final DistanceStrategy distanceStrategy;

    DistanceStrategyFactory(Predicate<Integer> predicate, DistanceStrategy distanceStrategy) {
        this.predicate = predicate;
        this.distanceStrategy = distanceStrategy;
    }

    public static DistanceStrategy getDistanceStrategy(int distance) {
        return Arrays.stream(values())
                .filter(strategy -> strategy.predicate.test(distance))
                .map(strategy -> strategy.distanceStrategy)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MATCHED_DISTANCE));
    }
}
