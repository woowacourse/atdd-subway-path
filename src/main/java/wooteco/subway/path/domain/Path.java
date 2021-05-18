package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final double distance;

    public Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> stations() {
        return stations;
    }

    public double distance() {
        return distance;
    }
}
