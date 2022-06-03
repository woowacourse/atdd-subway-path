package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum Fare {

    DEFAULT(distance -> distance <= 10, (distance, fare) -> fare),
    FIRST_RANGE_ADDED_EXTRA(
            distance -> distance > 10 && distance <= 50,
            (distance, fare) -> fare + addExtraFare(distance, 5, 10)
    ),
    SECOND_RANGE_ADDED_EXTRA(
            distance -> distance > 50,
            (distance, fare) ->
                    fare + addExtraFare(50, 5, 10) + addExtraFare(distance, 8, 50)
    )
    ;

    private static final double DEFAULT_FARE = 1250;
    private static final double ADDITIONAL_AMOUNT = 100;

    private final Predicate<Double> condition;
    private final BiFunction<Double, Double, Double> calculate;

    Fare(final Predicate<Double> condition, final BiFunction<Double, Double, Double> calculate) {
        this.condition = condition;
        this.calculate = calculate;
    }

    public static double calculate(final double distance, final int extraFare) {
        return Arrays.stream(Fare.values())
                .filter(fare -> fare.condition.test(distance))
                .map(fare -> fare.calculate.apply(distance, DEFAULT_FARE))
                .map(fare -> fare + extraFare)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리 정보가 올바르지 않습니다."));
    }

    private static double addExtraFare(final double distance, final double distanceUnit, final double limit) {
        return Math.ceil((distance - limit) / distanceUnit) * ADDITIONAL_AMOUNT;
    }
}
