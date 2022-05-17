package wooteco.subway.domain;

import java.util.List;

public class Path {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_DISTANCE = 10;

    private static final int ADDITIONAL_UNIT_FARE = 100;
    private static final int FIRST_ADDITIONAL_UNIT_DISTANCE = 5;
    private static final int OVER_ADDITIONAL_UNIT_DISTANCE = 8;
    private static final int FIRST_ADDITIONAL_FARE_DISTANCE = 50;

    private final List<Station> stations;
    private final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public int calculateFare() {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= FIRST_ADDITIONAL_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateFirstAdditionalFare();
        }
        return DEFAULT_FARE + calculateFirstAdditionalMaxFare() + calculateOverAdditionalFare();
    }

    private int calculateOverFare(int distance, int unitDistance) {
        return (int) ((Math.ceil((distance - 1.0) / unitDistance) + 1) * ADDITIONAL_UNIT_FARE);
    }

    private int calculateFirstAdditionalFare() {
        return calculateOverFare(distance - DEFAULT_FARE_DISTANCE, FIRST_ADDITIONAL_UNIT_DISTANCE);
    }

    private int calculateFirstAdditionalMaxFare() {
        return calculateOverFare(FIRST_ADDITIONAL_FARE_DISTANCE - DEFAULT_FARE_DISTANCE,
                FIRST_ADDITIONAL_UNIT_DISTANCE);
    }

    private int calculateOverAdditionalFare() {
        return calculateOverFare(distance - FIRST_ADDITIONAL_FARE_DISTANCE, OVER_ADDITIONAL_UNIT_DISTANCE);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
