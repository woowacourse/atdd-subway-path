package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.station.Station;

public class Path {

    private List<Station> route;
    private Distance distance;

    public Path(List<Station> route, long distanceValue) {
        this(route, new Distance(distanceValue));
    }

    public Path(List<Station> route, Distance distance) {
        this.route = route;
        this.distance = distance;
    }

    public List<Station> getRoute() {
        return route;
    }

    public Distance getDistance() {
        return distance;
    }
}
