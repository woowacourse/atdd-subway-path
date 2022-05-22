package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Line;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int MIDDLE_RANGE_BOUND = 10;
    private static final int LONG_RANGE_BOUND = 50;
    private static final int NOTHING = 0;
    private static final int DISTANCE_PER_FARE_STEP_AT_MIDDLE_RANGE = 5;
    private static final int DISTANCE_PER_FARE_STEP_AT_LONG_RANGE = 8;

    public int findFare(int distance, List<Line> lines, int age) {
        int lineExtraFare = lines.stream()
                .map(Line::getExtraFare)
                .max(Integer::compare)
                .orElse(NOTHING);
        int extraFare = distanceExtraFare(distance) + lineExtraFare;
        return AgeGroup.discount(age, BASE_FARE + extraFare);
    }

    private int distanceExtraFare(int distance) {
        return ADDITIONAL_FARE * extraFareCount(distance);
    }

    private int extraFareCount(int distance) {
        if (distance > LONG_RANGE_BOUND) {
            return ExtraFareCountOverLongRange(distance);
        }

        if (distance > MIDDLE_RANGE_BOUND) {
            return extraFareCountOverMiddleRange(distance);
        }

        return NOTHING;
    }

    private int ExtraFareCountOverLongRange(int distance) {
        int remain = distance - LONG_RANGE_BOUND;
        int count = remain / DISTANCE_PER_FARE_STEP_AT_LONG_RANGE;
        if (remain % DISTANCE_PER_FARE_STEP_AT_LONG_RANGE != 0) {
            count++;
        }

        return count + extraFareCountOverMiddleRange(distance - remain);
    }

    private int extraFareCountOverMiddleRange(int distance) {
        int remain = distance - MIDDLE_RANGE_BOUND;
        int count = remain / DISTANCE_PER_FARE_STEP_AT_MIDDLE_RANGE;
        if (remain % DISTANCE_PER_FARE_STEP_AT_MIDDLE_RANGE != 0) {
            count++;
        }

        return count;
    }
}
