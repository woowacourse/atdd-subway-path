package wooteco.subway.domain.graph;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.domain.station.Station;

public class Route {

    private final List<Station> route;
    private final int distance;

    public Route(List<Station> route, int distance) {
        this.route = route;
        this.distance = distance;
    }

    public List<Station> getRoute() {
        return new ArrayList<>(route);
    }

    public int getDistance() {
        return distance;
    }
}
