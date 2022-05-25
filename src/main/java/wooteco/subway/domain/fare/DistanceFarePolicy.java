package wooteco.subway.domain.fare;

import java.util.Arrays;

public enum DistanceFarePolicy {

    SHORT_DISTANCE_POLICY {
        @Override
        boolean condition(int distance) {
            return distance >= 0 && distance <= FIRST_FARE_INCREASE_STANDARD;
        }

        @Override
        int calculate(int distance) {
            return BASIS_FARE;
        }
    },

    MIDDLE_DISTANCE_POLICY {
        @Override
        boolean condition(int distance) {
            return distance > FIRST_FARE_INCREASE_STANDARD && distance <= LAST_FARE_INCREASE_STANDARD;
        }

        @Override
        int calculate(int distance) {
            return BASIS_FARE + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - FIRST_FARE_INCREASE_STANDARD) / FIRST_FARE_INCREASE_STANDARD_UNIT);
        }
    },

    LONG_DISTANCE_POLICY {
        @Override
        boolean condition(int distance) {
            return distance > FIRST_FARE_INCREASE_STANDARD;
        }

        @Override
        int calculate(int distance) {
            return FARE_AT_50KM + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - LAST_FARE_INCREASE_STANDARD) / LAST_FARE_INCREASE_STANDARD_UNIT);
        }
    };

    private static final int BASIS_FARE = 1_250;
    private static final int FARE_AT_50KM = 2_050;
    private static final int FIRST_FARE_INCREASE_STANDARD = 10;
    private static final int LAST_FARE_INCREASE_STANDARD = 50;
    private static final int FIRST_FARE_INCREASE_STANDARD_UNIT = 5;
    private static final int LAST_FARE_INCREASE_STANDARD_UNIT = 8;
    private static final int INCREASE_RATE = 100;

    abstract boolean condition(int distance);

    abstract int calculate(int distance);

    public static int calculateFare(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.condition(distance))
                .map(distanceFarePolicy -> distanceFarePolicy.calculate(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("거리를 잘못 입력하였습니다."));
    }
}
