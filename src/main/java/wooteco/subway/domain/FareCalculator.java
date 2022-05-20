package wooteco.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    private static final long BASE_FARE = 1250L;
    private static final int SECOND_LEVEL_BASE_DISTANCE = 10;
    private static final int SECOND_LEVEL_DISTANCE_UNIT = 5;
    private static final long SECOND_LEVEL_ADDITIONAL_FARE = 100L;
    private static final int THIRD_LEVEL_BASE_DISTANCE = 50;
    private static final int THIRD_LEVEL_DISTANCE_UNIT = 8;
    private static final long THIRD_LEVEL_ADDITIONAL_FARE = 100L;
    private static final int ADDITIONAL_FOR_CEILING = 1;

    private FareCalculator() {
    }

    public Long calculate(int distance) {
        if (distance <= SECOND_LEVEL_BASE_DISTANCE) {
            return BASE_FARE;
        }

        if (distance <= THIRD_LEVEL_BASE_DISTANCE) {
            return BASE_FARE + calculateSecondLevelAdditionalUnits(distance) * SECOND_LEVEL_ADDITIONAL_FARE;
        }

        return calculateThirdLevelBaseFare() + calculateThirdLevelUnits(distance) * THIRD_LEVEL_ADDITIONAL_FARE;
    }

    private int calculateSecondLevelAdditionalUnits(int distance) {
        final int secondLevelDistance = distance - SECOND_LEVEL_BASE_DISTANCE - ADDITIONAL_FOR_CEILING;
        return secondLevelDistance / SECOND_LEVEL_DISTANCE_UNIT + ADDITIONAL_FOR_CEILING;
    }

    private long calculateThirdLevelBaseFare() {
        return BASE_FARE + additionalBetweenSecondAndThird() * SECOND_LEVEL_ADDITIONAL_FARE;
    }

    private int additionalBetweenSecondAndThird() {
        return (THIRD_LEVEL_BASE_DISTANCE - SECOND_LEVEL_BASE_DISTANCE) / SECOND_LEVEL_DISTANCE_UNIT;
    }

    private int calculateThirdLevelUnits(int distance) {
        return calculateThirdLevelDistance(distance) / THIRD_LEVEL_DISTANCE_UNIT + ADDITIONAL_FOR_CEILING;
    }

    private int calculateThirdLevelDistance(int distance) {
        return distance - THIRD_LEVEL_BASE_DISTANCE - ADDITIONAL_FOR_CEILING;
    }
}
