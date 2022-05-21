package wooteco.subway.domain;

import java.util.List;

public class Path {

    private static final int DEFAULT_FARE = 1250;
    private static final int UNIT_OF_ADDITIONAL_FARE = 100;

    private static final int DISTANCE_OF_DEFAULT_FARE = 10;
    private static final int DISTANCE_OF_FIRST_ADDITIONAL_UNIT = 5;
    private static final int DISTANCE_OF_OVER_ADDITIONAL_UNIT = 8;
    private static final int DISTANCE_OF_OVER_ADDITIONAL_FARE = 50;
    private static final int DISCOUNT_AMOUNT = 350;
    private static final int CHILD_MINIMUM_AGE = 6;
    private static final int CHILD_MAXIMUM_AGE = 13;
    private static final int YOUTH_MINIMUM_AGE = 13;
    private static final int YOUTH_MAXIMUM_AGE = 19;
    private static final double YOUTH_DISCOUNT_RATE = 0.8;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    private final List<Station> stations;
    private final int distance;
    private final List<Line> lines;

    public Path(List<Station> stations, int distance, List<Line> lines) {
        validateEmptyStations(stations);
        validatePositiveDistance(distance);
        validateEmptyLines(lines);
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public void validateEmptyStations(final List<Station> stations) {
        if (stations.isEmpty()) {
            throw new IllegalArgumentException("경로는 비어서는 안됩니다.");
        }
    }

    public void validatePositiveDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야합니다.");
        }
    }

    private void validateEmptyLines(List<Line> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("이용한 노선은 비어서는 안됩니다.");
        }
    }

    public double calculateFinalFare(int age) {
        if (age >= CHILD_MINIMUM_AGE && age < CHILD_MAXIMUM_AGE) {
            return (calculateFare() - DISCOUNT_AMOUNT) * CHILD_DISCOUNT_RATE;
        }
        if (age >= YOUTH_MINIMUM_AGE && age < YOUTH_MAXIMUM_AGE) {
            return (calculateFare() - DISCOUNT_AMOUNT) * YOUTH_DISCOUNT_RATE;
        }
        return calculateFare();
    }

    private int calculateFare() {
        if (distance <= DISTANCE_OF_DEFAULT_FARE) {
            return DEFAULT_FARE + calculateExtraFare();
        }
        if (distance <= DISTANCE_OF_OVER_ADDITIONAL_FARE) {
            return DEFAULT_FARE + calculateFirstAdditionalFare() + calculateExtraFare();
        }
        return DEFAULT_FARE + calculateFirstAdditionalMaxFare() + calculateOverAdditionalFare() + calculateExtraFare();
    }

    private int calculateFirstAdditionalFare() {
        return calculateOverFare(distance - DISTANCE_OF_DEFAULT_FARE, DISTANCE_OF_FIRST_ADDITIONAL_UNIT);
    }

    private int calculateOverFare(int distance, int unitDistance) {
        return (((distance - 1) / unitDistance) * UNIT_OF_ADDITIONAL_FARE) + UNIT_OF_ADDITIONAL_FARE;
    }

    private int calculateFirstAdditionalMaxFare() {
        return calculateOverFare(DISTANCE_OF_OVER_ADDITIONAL_FARE - DISTANCE_OF_DEFAULT_FARE,
                DISTANCE_OF_FIRST_ADDITIONAL_UNIT
        );
    }

    private int calculateOverAdditionalFare() {
        return calculateOverFare(distance - DISTANCE_OF_OVER_ADDITIONAL_FARE, DISTANCE_OF_OVER_ADDITIONAL_UNIT
        );
    }

    private int calculateExtraFare() {
        int minExtraFare = lines.stream()
                .map(Line::getExtraFare)
                .mapToInt(fare -> fare)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("존재하는 노선 요금이 없습니다."));
        return minExtraFare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
