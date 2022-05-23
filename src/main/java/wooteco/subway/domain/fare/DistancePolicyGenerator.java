package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import wooteco.subway.domain.fare.policy.distance.OverFiftyKMPolicy;
import wooteco.subway.domain.fare.policy.distance.TenToFiftyKMPolicy;
import wooteco.subway.domain.fare.policy.distance.UnderTenKMPolicy;

public enum DistancePolicyGenerator {
    UNDER_TEN((distance) -> 0 <= distance && distance < 10,
            new UnderTenKMPolicy()),
    TEN_TO_FIFTY((distance) -> 10 <= distance && distance < 50,
            new TenToFiftyKMPolicy()),
    OVER_FIFTY((distance) -> distance >= 50,
            new OverFiftyKMPolicy());

    private static final String NO_DISTANCE_POLICY_ERROR_MESSAGE = "해당되는 거리 정책이 없습니다.";

    private final Predicate<Integer> predicate;
    private final DistancePolicy distancePolicy;

    DistancePolicyGenerator(Predicate<Integer> predicate, DistancePolicy distancePolicy) {
        this.predicate = predicate;
        this.distancePolicy = distancePolicy;
    }

    public static DistancePolicy of(int distance) {
        return Arrays.stream(DistancePolicyGenerator.values())
                .filter(generator -> generator.predicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_DISTANCE_POLICY_ERROR_MESSAGE))
                .distancePolicy;
    }
}
