package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class ShortestPath {
    private final List<Station> path;
    private final int distance;

    public ShortestPath(List<Station> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
