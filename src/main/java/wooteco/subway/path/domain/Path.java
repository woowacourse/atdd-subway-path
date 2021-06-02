package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<Station> shortestPath;
    private final int distance;

    public Path(List<Station> shortestPath, double distance) {
        this.shortestPath = shortestPath;
        this.distance = (int) distance;
    }

    public List<Station> getStationPath() {
        return shortestPath;
    }

    public int getDistance() {
        return distance;
    }
}
