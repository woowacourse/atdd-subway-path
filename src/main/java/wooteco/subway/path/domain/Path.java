package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Station> stations;
    private int distance;

    public Path() {
    }

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}