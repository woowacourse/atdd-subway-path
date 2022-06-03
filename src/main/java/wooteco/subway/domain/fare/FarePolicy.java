package wooteco.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FarePolicy {
    LESS_THAN_10KM(distance -> distance <= 10) {
        @Override
        public int calculate(int distance) {
            return DEFAULT_FARE_LESS_THAN_50;
        }
    },
    BETWEEN_10KM_AND_50KM(distance -> distance > 10 && distance <= 50) {
        @Override
        public int calculate(int distance) {
            return DEFAULT_FARE_LESS_THAN_50 + calculateAdditionalFare(distance, 10, 5);
        }
    },
    MORE_THAN_50KM(distance -> distance > 50) {
        @Override
        public int calculate(int distance) {
            return DEFAULT_FARE_MORE_THAN_50 + calculateAdditionalFare(distance, 50, 8);
        }
    };

    private static final int DEFAULT_FARE_LESS_THAN_50 = 1250;
    private static final int DEFAULT_FARE_MORE_THAN_50 = DEFAULT_FARE_LESS_THAN_50 + 800;

    private final Predicate<Integer> predicate;

    FarePolicy(Predicate<Integer> predicate) {
        this.predicate = predicate;
    }

    abstract public int calculate(int distance);

    public static int calculateFare(int distance, int extraFare) {
        FarePolicy farePolicy = FarePolicy.of(distance);
        return farePolicy.calculate(distance) + extraFare;
    }

    private static FarePolicy of(int distance) {
        return Arrays.stream(values())
                .filter(it -> it.predicate.test(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 되는 요금 정책이 없습니다."));
    }

    private static int calculateAdditionalFare(int distance, int threshold, int unit) {
        return (int) ((Math.ceil((distance - (threshold + 1)) / unit) + 1) * 100);
    }
}
