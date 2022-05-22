package wooteco.subway.domain;

import java.util.List;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_EXTRA_LINE_FARE = 0;
    private static final int ADDITIONAL_DISTANCE_PER_5KM = 10;
    private static final int ADDITIONAL_DISTANCE_PER_8KM = 51;
    private static final int DISTANCE_UNIT_UNDER_50 = 5;
    private static final int DISTANCE_UNIT_OVER_50 = 8;
    private static final int ADDITIONAL_AMOUNT = 100;

    public int calculate(final int distance, final List<Station> stations, final List<Line> lines) {
        final int extraLineFare = calculateExtraLineFare(stations, lines);
        int fare = DEFAULT_FARE + extraLineFare;
        if (distance >= ADDITIONAL_DISTANCE_PER_5KM && distance < ADDITIONAL_DISTANCE_PER_8KM) {
            return fare + addExtraFare(distance, DISTANCE_UNIT_UNDER_50, ADDITIONAL_DISTANCE_PER_5KM);
        }
        if (distance >= ADDITIONAL_DISTANCE_PER_8KM) {
            return fare
                    + addExtraFare(ADDITIONAL_DISTANCE_PER_8KM - 1, DISTANCE_UNIT_UNDER_50, ADDITIONAL_DISTANCE_PER_5KM)
                    + addExtraFare(distance, DISTANCE_UNIT_OVER_50, ADDITIONAL_DISTANCE_PER_8KM - 1);
        }
        return fare;
    }

    private int calculateExtraLineFare(final List<Station> stations, final List<Line> lines) {
        int maxExtraLineFare = DEFAULT_EXTRA_LINE_FARE;
        for (Line line : lines) {
            maxExtraLineFare = Math.max(maxExtraLineFare, findExtraLineFare(line, stations));
        }
        return maxExtraLineFare;
    }

    private int findExtraLineFare(final Line line, final List<Station> stations) {
        final boolean doesLineContainStation = stations.stream()
                .anyMatch(line::containStation);
        if (doesLineContainStation) {
            return line.getExtraFare();
        }
        return DEFAULT_EXTRA_LINE_FARE;
    }

    private int addExtraFare(final int distance, final int distanceUnit, final int limit) {
        return (int) Math.ceil((double) (distance - limit) / distanceUnit) * ADDITIONAL_AMOUNT;
    }
}
