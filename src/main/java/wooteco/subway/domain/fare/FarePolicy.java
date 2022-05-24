package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FarePolicy {

    BASE(distance -> distance <= 10, 1250),
    FIRST_CHARGED(distance -> distance > 10 && distance <= 50, 1250),
    SECOND_CHARGED(distance -> distance > 50, 2050),
    ;

    private static final int FIRST_EXTRA_FARE_STANDARD = 5;
    private static final int SECOND_EXTRA_FARE_STANDARD = 8;
    private static final int EXTRA_FARE = 100;

    private final Predicate<Double> predicate;
    private final int baseFare;

    FarePolicy(final Predicate<Double> predicate, final int baseFare) {
        this.predicate = predicate;
        this.baseFare = baseFare;
    }

    public static FarePolicy choosePolicy(final double distance) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("거리는 0또는 양의 정수로 입력해야합니다."));
    }

    public int calculateFare(final double distance) {
        if (distance > 50) {
            return this.baseFare + addSecondExtraFare(distance - 50);
        }
        if (distance > 10) {
            return this.baseFare + addFirstExtraFare(distance - 10);
        }
        return this.baseFare;
    }

    private int addFirstExtraFare(final double distance) {
        return (int) ((Math.ceil((distance) / FIRST_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }

    private int addSecondExtraFare(final double distance) {
        return (int) ((Math.ceil((distance) / SECOND_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }
}
