package wooteco.subway.domain.fare;

import java.util.List;
import java.util.NoSuchElementException;
import wooteco.subway.domain.Line;

public final class FarePolicyImpl implements FarePolicy {

    private static final int STANDARD_DISTANCE = 10;
    private static final int STANDARD_FARE = 1250;
    private static final int OVER_FARE_UNIT = 100;
    private static final int FIRST_OVER_FARE_DISTANCE = 50;
    private static final int FIRST_OVER_DISTANCE_UNIT = 5;
    private static final int SECOND_OVER_DISTANCE_UNIT = 8;

    @Override
    public Fare calculateFare(int distance, List<Line> lines) {
        return new Fare(calculateDistanceFare(distance) + calculateExtraFare(lines));
    }

    private static int calculateDistanceFare(int distance) {
        if (distance <= STANDARD_DISTANCE) {
            return STANDARD_FARE;
        }
        if (distance <= FIRST_OVER_FARE_DISTANCE) {
            return STANDARD_FARE
                    + calculateOverFare(distance - STANDARD_DISTANCE, FIRST_OVER_DISTANCE_UNIT);
        }
        return STANDARD_FARE
                + calculateOverFare(FIRST_OVER_FARE_DISTANCE - STANDARD_DISTANCE, FIRST_OVER_DISTANCE_UNIT)
                + calculateOverFare(distance - FIRST_OVER_FARE_DISTANCE, SECOND_OVER_DISTANCE_UNIT);
    }

    private static int calculateOverFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * OVER_FARE_UNIT);
    }

    private static int calculateExtraFare(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new NoSuchElementException("최대값을 찾을 수 없습니다."));
    }
}
