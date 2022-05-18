package wooteco.subway.domain;

import java.util.List;

public class Path {
    private static final int BASE_FARE = 1250;
    private static final int MINIMUM_BOUNDARY = 10;
    private static final int MAXIMUM_BOUNDARY = 50;
    private static final int OVER_TEN_POLICY = 5;
    private static final int OVER_FIFTY_POLICY = 8;
    private static final int SURCHARGE = 100;

    private final List<Station> stations;
    private final int distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Station> stations, double distance) {
        return new Path(stations, (int) Math.floor(distance));
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int calculateFare() {
        int fare = BASE_FARE;

        fare = belowMaximumBoundary(fare);
        fare = overMaximumBoundary(fare);

        return fare;
    }

    private int belowMaximumBoundary(int fare) {
        if (MINIMUM_BOUNDARY < distance && distance <= MAXIMUM_BOUNDARY) {
            fare += calculateOverFare(distance - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
        }
        return fare;
    }

    private int overMaximumBoundary(int fare) {
        if (MAXIMUM_BOUNDARY < distance) {
            fare += calculateOverFare(MAXIMUM_BOUNDARY - MINIMUM_BOUNDARY, OVER_TEN_POLICY);
            fare += calculateOverFare(distance - MAXIMUM_BOUNDARY, OVER_FIFTY_POLICY);
        }
        return fare;
    }

    private int calculateOverFare(int overDistance, int policy) {
        return (int)((Math.ceil((overDistance - 1) / policy) + 1) * SURCHARGE);
    }
}
