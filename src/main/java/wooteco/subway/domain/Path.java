package wooteco.subway.domain;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1250;

    private final List<Station> routeStations;
    private final int distance;

    public Path(final List<Station> routeStations, final int distance) {
        this.routeStations = routeStations;
        this.distance = distance;
    }

    public int calculateFare() {
        int tempDistance = distance;
        int fare = BASIC_FARE;
        if (tempDistance > 50) {
            fare += calculateOverFare(tempDistance - 50, 8);
            tempDistance = 50;
        }
        if (tempDistance > 10) {
            fare += calculateOverFare(tempDistance - 10, 5);
        }
        return fare;
    }

    private int calculateOverFare(int distance, int kilo) {
        return (int) ((Math.ceil((distance - 1) / kilo) + 1) * 100);
    }

    public List<Station> getRouteStations() {
        return routeStations;
    }

    public int getDistance() {
        return distance;
    }
}
