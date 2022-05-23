package wooteco.subway.domain;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1250;

    private final List<Station> routeStations;
    private final int distance;
    private final int extraFare;

    public Path(final List<Station> routeStations, final int distance, final int extraFare) {
        this.routeStations = routeStations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public int calculateFare(final int age) {
        final int fare = calculateBasicFare();
        return calculateAgeDiscount(fare, age) + extraFare;
    }

    private int calculateBasicFare() {
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

    private int calculateOverFare(final int distance, final int kilo) {
        return (int) ((Math.ceil((distance - 1) / kilo) + 1) * 100);
    }

    private int calculateAgeDiscount(final int fare, final int age) {
        if (age < 6) {
            return 0;
        }
        if (age < 13) {
            return fare - (int) ((fare - 350) * 0.5);
        }
        if (age < 19) {
            return fare - (int) ((fare - 350) * 0.2);
        }
        return fare;
    }

    public List<Station> getRouteStations() {
        return routeStations;
    }

    public int getDistance() {
        return distance;
    }
}
