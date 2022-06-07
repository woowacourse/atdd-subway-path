package wooteco.subway.domain;

import java.util.Arrays;

public enum DistanceFare {
    LONG_DISTANCE(2050, 51, 8, 100),
    MIDDLE_DISTANCE(1250, 11, 5, 100),
    BASIC_DISTANCE(1250, 1, 1, 0);

    private final int defaultFare;
    private final int leastDistance;
    private final int additionalFareDistanceUnit;
    private final int extraMoneyUnit;

    DistanceFare(int defaultFare, int leastDistance, int additionalFareDistanceUnit, int extraMoneyUnit) {
        this.defaultFare = defaultFare;
        this.leastDistance = leastDistance;
        this.additionalFareDistanceUnit = additionalFareDistanceUnit;
        this.extraMoneyUnit = extraMoneyUnit;
    }

    public static DistanceFare valueOf(int distance) {
        return Arrays.stream(values())
                .filter(value -> value.leastDistance <= distance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력받은 거리값에 따른 요금 정책을 찾을 수 없습니다."));
    }

    public int calculateFare(int distance) {
        return defaultFare + calculateExtraFare(distance);
    }

    public int calculateExtraFare(int distance) {
        return (int) ((Math.ceil(distance - leastDistance) / additionalFareDistanceUnit) + 1) * extraMoneyUnit;
    }
}
