package wooteco.subway.domain.fare.policy.distance;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistancePolicyGenerator {
    UNDER_TEN((distance) ->
            Constants.MIN_DISTANCE <= distance && distance < Constants.TEN_DISTANCE,
            UnderTenKMPolicy::new),
    TEN_TO_FIFTY((distance) ->
            Constants.TEN_DISTANCE <= distance && distance < Constants.FIFTY_DISTANCE,
            TenToFiftyKMPolicy::new),
    OVER_FIFTY((distance) ->
            distance >= Constants.FIFTY_DISTANCE,
            OverFiftyKMPolicy::new);

    private static final String NO_DISTANCE_POLICY_ERROR_MESSAGE = "해당되는 거리 정책이 없습니다.";

    private final Predicate<Integer> predicate;
    private final Function<Integer, BasePolicy> distancePolicy;

    DistancePolicyGenerator(Predicate<Integer> predicate, Function<Integer, BasePolicy> distancePolicy) {
        this.predicate = predicate;
        this.distancePolicy = distancePolicy;
    }

    public static BasePolicy of(int distance) {
        return Arrays.stream(DistancePolicyGenerator.values())
                .filter(generator -> generator.predicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_DISTANCE_POLICY_ERROR_MESSAGE))
                .distancePolicy.apply(distance);
    }

    private static class Constants {
        private static final int MIN_DISTANCE = 0;
        private static final int TEN_DISTANCE = 10;
        private static final int FIFTY_DISTANCE = 50;
    }
}
