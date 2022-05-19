package wooteco.subway.domain;

public class FareCalculator {

    private static final int PRIMARY_BASIC_DISTANCE = 10;
    private static final int SECONDARY_BASIC_DISTANCE = 50;
    private static final int OVER_FARE_PER_COUNT = 100;
    private static final int BASIC_FARE = 1250;

    public int calculateFare(int distance) {
        return BASIC_FARE + overDistanceFare(distance);
    }

    private int overDistanceFare(int distance) {
        if (isOverSecondaryBasicDistance(distance)) {
            return overSecondaryBasicDistanceFare(distance) +
                overPrimaryBasicDistanceFare(SECONDARY_BASIC_DISTANCE);
        }

        if (isOverPrimaryBasicDistance(distance)) {
            return overPrimaryBasicDistanceFare(distance);
        }

        return 0;
    }

    private boolean isOverSecondaryBasicDistance(int distance) {
        return distance > SECONDARY_BASIC_DISTANCE;
    }

    private boolean isOverPrimaryBasicDistance(int distance) {
        return distance > PRIMARY_BASIC_DISTANCE;
    }

    private int overSecondaryBasicDistanceFare(int distance) {
        int remainDistance = distance - SECONDARY_BASIC_DISTANCE;
        int overDistancePerCount = 8;
        return overDistanceFare(remainDistance, overDistancePerCount);
    }

    private int overPrimaryBasicDistanceFare(int distance) {
        int remainDistance = distance - PRIMARY_BASIC_DISTANCE;
        int overDistancePerCount = 5;
        return overDistanceFare(remainDistance, overDistancePerCount);
    }

    private int overDistanceFare(int distance, int overDistancePerCount) {
        int count = distance / overDistancePerCount;
        if (distance % overDistancePerCount != 0) {
            count++;
        }
        return OVER_FARE_PER_COUNT * count;
    }
}