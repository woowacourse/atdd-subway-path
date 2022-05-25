package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceFareCalculator {
    OVER_FIFTY((distance) -> distance > 50, (distance) -> getExtraFare(distance - 50, 8)),
    OVER_TEN((distance) -> distance > 10, (distance) -> getExtraFare(distance, 5, 50, 10)),
    OVER_ZERO((distance) -> distance > 0, (distance) -> 1250);

    private final Predicate<Integer> condition;
    private final Function<Integer, Integer> calculate;

    DistanceFareCalculator(Predicate<Integer> condition,
        Function<Integer, Integer> calculate) {
        this.condition = condition;
        this.calculate = calculate;
    }

    private static int getExtraFare(int distance, int unitDistance) {
        int fare = 0;
        fare += distance / unitDistance * 100;
        if (distance % unitDistance > 0) {
            fare += 100;
        }
        return fare;
    }

    private static int getExtraFare(int distance, int unitDistance, int overStandard, int standard) {
        if (distance > overStandard) {
            distance = overStandard;
        }
        return getExtraFare(distance - standard, unitDistance);
    }

    public static int calculateFare(int distance) {
        return Arrays.stream(values())
            .mapToInt(distanceFareCalculator -> calculateExtraFare(distanceFareCalculator, distance))
            .sum();
    }

    private static int calculateExtraFare(DistanceFareCalculator distanceFareCalculator, int distance) {
        if (!distanceFareCalculator.condition.test(distance)) {
            return 0;
        }
        return distanceFareCalculator.calculate.apply(distance);
    }
}
