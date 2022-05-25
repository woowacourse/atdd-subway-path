package wooteco.subway.domain.farepolicy;

import java.util.Arrays;

public enum DistanceRange {

    BASIC_DISTANCE(1, 10),
    SHORT_RANGE(11, 50),
    LONG_RANGE(51, Integer.MAX_VALUE);

    private final int minDistance;
    private final int maxDistance;

    DistanceRange(int minDistance, int maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    public static DistanceRange from(final int distance) {
        return Arrays.stream(values())
                .filter(ageRange -> ageRange.minDistance <= distance && distance <= ageRange.maxDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리는 1 이상의 정수여야 합니다."));
    }

    public int maxDistance() {
        return maxDistance;
    }
}
