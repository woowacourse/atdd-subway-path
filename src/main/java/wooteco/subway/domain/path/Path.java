package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;

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

    public int calculateFare(int age) {
        int tempDistance = distance;
        int fare = BASIC_FARE + extraFare;
        if (tempDistance > 50) {
            fare += calculateOverFare(tempDistance - 50, 8);
            tempDistance = 50;
        }
        if (tempDistance > 10) {
            fare += calculateOverFare(tempDistance - 10, 5);
        }
        fare -= AgeDiscountPolicy.from(age).calculateDiscountAmount(fare);
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
