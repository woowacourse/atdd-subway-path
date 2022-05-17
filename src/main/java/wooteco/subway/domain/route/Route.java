package wooteco.subway.domain.route;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.station.Station;

public class Route {

    private final List<Station> route;
    private final double distance;

    public Route(List<Station> route, double distance) {
        this.route = route;
        this.distance = distance;
    }

    public List<Station> getRoute() {
        return new ArrayList<>(route);
    }

    public double getDistance() {
        return distance;
    }
}
