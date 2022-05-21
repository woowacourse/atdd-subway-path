package wooteco.subway.domain;

import java.util.Map;

public class FareCalculator {

    private static final int PRIMARY_BASIC_DISTANCE = 10;
    private static final int SECONDARY_BASIC_DISTANCE = 50;
    private static final int COUNT_PER_OVER_FARE = 100;
    private static final int BASIC_FARE = 1250;

    private final Map<Long, Integer> extraFares;
    private final Passenger passenger;

    public FareCalculator(Map<Long, Integer> extraFares, Passenger passenger) {
        this.extraFares = extraFares;
        this.passenger = passenger;
    }

    public int calculateFare(Path path) {
        return passenger.calculateFare(BASIC_FARE + extraFare(path));
    }

    private int extraFare(Path path) {
        return overDistanceFare(path.getDistance()) + extraLineFare(path);
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

    private int extraLineFare(Path path) {
        return path.getSections().stream()
            .map(Section::getLineId)
            .mapToInt(extraFares::get)
            .max()
            .orElse(0);
    }
}