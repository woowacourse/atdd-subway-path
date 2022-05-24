package wooteco.subway.domain;

import java.util.Arrays;
import wooteco.subway.exception.ClientException;

public enum DistanceRange {

    UNTIL_9(0, 9, 9, 1250),
    FROM_10_UNTIL_50(10, 50, 5, 1250),
    OVER_50(50, Integer.MAX_VALUE, 8, 2050);

    private final int min;
    private final int max;
    private final int chargeUnit;
    private final int basicFare;

    DistanceRange(final int min, final int max, final int chargeUnit, final int basicFare) {
        this.min = min;
        this.max = max;
        this.chargeUnit = chargeUnit;
        this.basicFare = basicFare;
    }

    public static DistanceRange findRange(int distance) {
        return Arrays.stream(DistanceRange.values())
                .filter(it -> it.isInclude(distance))
                .findFirst()
                .orElseThrow(() -> new ClientException("[ERROR] 존재하지 않는 범위입니다."));
    }

    public boolean isInclude(int distance) {
        return distance >= min && distance <= max;
    }

    public int getFareByDistance(int distance) {
        if (this == UNTIL_9) {
            return basicFare;
        }
        int fare = 0;
        fare += ((distance - min) / chargeUnit) * 100;
        if ((distance - min) % chargeUnit > 0 || ((distance - min) / chargeUnit == 0)) {
            fare += 100;
        }
        return basicFare + fare;
    }
}
