package wooteco.subway.domain.fare.farepolicy;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareCalculator {

    FREE(distance -> distance == 0, new FreeFarePolicyStrategy()),
    BASIC(distance -> distance > 0 && distance <= 10, new BasicFarePolicyStrategy()),
    FIRST_ADD(distance -> distance > 10 && distance <= 50, new FirstAddFarePolicyStrategy()),
    SECOND_ADD(distance -> distance > 50, new SecondAddFarePolicyStrategy());

    private static final String NOT_EXIST_MATCHED_DISTANCE = "일치하는 거리가 없습니다.";

    private final Predicate<Integer> predicate;
    private final FarePolicyStrategy farePolicyStrategy;

    FareCalculator(Predicate<Integer> predicate, FarePolicyStrategy farePolicyStrategy) {
        this.predicate = predicate;
        this.farePolicyStrategy = farePolicyStrategy;
    }

    public static int calculateFare(int distance) {
        return getFarePolicyStrategy(distance).calculateFare(distance);
    }

    private static FarePolicyStrategy getFarePolicyStrategy(int distance) {
        return Arrays.stream(values())
                .filter(policy -> policy.predicate.test(distance))
                .map(policy -> policy.farePolicyStrategy)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_MATCHED_DISTANCE));
    }
}
