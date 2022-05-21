package wooteco.subway.domain;

public class FareCalculator {

    private static final int PRIMARY_BASIC_DISTANCE = 10;
    private static final int SECONDARY_BASIC_DISTANCE = 50;
    private static final int COUNT_PER_OVER_FARE = 100;
    private static final int BASIC_FARE = 1250;

    public int calculateFare(Path path) {
        return BASIC_FARE + overDistanceFare(path.getDistance());
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
        return COUNT_PER_OVER_FARE * count;
    }
}