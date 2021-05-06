package wooteco.subway.path.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DistanceFarePolicy {
    OVER_TEN_TO_FIFTY(5, 100, 10, 50),

    OVER_FIFTY(8, 100, 50, Integer.MAX_VALUE);

    public static final int BASIC_DISTANCE = 10;
    public static final int EXTRA_DISTANCE = 50;
    private int unitDistance;
    private int unitFare;
    private Integer min;
    private Integer max;

    DistanceFarePolicy(int unitDistance, int unitFare, Integer min, Integer max) {
        this.unitDistance = unitDistance;
        this.unitFare = unitFare;
        this.min = min;
        this.max = max;
    }

    public static List<DistanceFarePolicy> findDistancePolicies(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.getMin() < distance)
                .collect(Collectors.toList());
    }

    public int calculateOverDistance(int distance) {
        if (distance <= this.min) {
            return 0;
        }
        int overDistance = distance - this.min;
        if (distance > this.max) {
            overDistance = this.max - this.min;
        }
        return overDistance;
    }

    public int getUnitDistance() {
        return unitDistance;
    }

    public int getUnitFare() {
        return unitFare;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
