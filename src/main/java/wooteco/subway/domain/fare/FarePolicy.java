package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.strategy.BaseCalculateStrategy;
import wooteco.subway.domain.fare.strategy.FareCalculateStrategy;
import wooteco.subway.domain.fare.strategy.FirstConditionCalculateStrategy;
import wooteco.subway.domain.fare.strategy.SecondConditionCalculateStrategy;

public enum FarePolicy {

    BASE(distance -> distance <= 10, new BaseCalculateStrategy()),
    FIRST_CHARGED(distance -> distance > 10 && distance <= 50, new FirstConditionCalculateStrategy()),
    SECOND_CHARGED(distance -> distance > 50, new SecondConditionCalculateStrategy()),
    ;

    private final Predicate<Double> distancePredicate;
    private final FareCalculateStrategy fareCalculateStrategy;

    FarePolicy(final Predicate<Double> distancePredicate, final FareCalculateStrategy fareCalculateStrategy) {
        this.distancePredicate = distancePredicate;
        this.fareCalculateStrategy = fareCalculateStrategy;
    }

    public static int calculateFare(final double distance) {
        FarePolicy farePolicy = Arrays.stream(values())
                .filter(value -> value.distancePredicate.test(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("거리는 0또는 양의 정수로 입력해야합니다."));
        return farePolicy.fareCalculateStrategy.calculateFare(distance);
    }
}
