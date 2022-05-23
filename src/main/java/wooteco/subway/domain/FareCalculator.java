package wooteco.subway.domain;

import wooteco.subway.exception.CalculatePathsException;

public class FareCalculator {

    private static final int FIRST_EXTRA_FARE_DISTANCE = 10;
    private static final int SECOND_EXTRA_FARE_DISTANCE = 50;
    private static final int FIRST_EXTRA_FARE_STANDARD = 5;
    private static final int SECOND_EXTRA_FARE_STANDARD = 8;
    private static final int MAX_FIRST_EXTRA_FARE = 800;
    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final double distance;

    public FareCalculator(final double distance) {
        validateDistanceOverThanZero(distance);
        this.distance = distance;
    }

    private void validateDistanceOverThanZero(final double distance) {
        if (distance <= 0) {
            throw new CalculatePathsException("최단 경로의 거리가 0이하 이기 때문에 요금을 계산 할 수 없습니다.");
        }
    }

    public int calculateFare() {
        if (distance > SECOND_EXTRA_FARE_DISTANCE) {
            return BASIC_FARE + MAX_FIRST_EXTRA_FARE + addSecondExtraFare(distance - SECOND_EXTRA_FARE_DISTANCE);
        }
        if (distance > FIRST_EXTRA_FARE_DISTANCE) {
            return BASIC_FARE + addFirstExtraFare(distance - FIRST_EXTRA_FARE_DISTANCE);
        }
        return BASIC_FARE;
    }

    private int addSecondExtraFare(final double distance) {
        return (int) ((Math.ceil((distance) / SECOND_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }

    private int addFirstExtraFare(final double distance) {
        return (int) ((Math.ceil((distance) / FIRST_EXTRA_FARE_STANDARD)) * EXTRA_FARE);
    }
}
