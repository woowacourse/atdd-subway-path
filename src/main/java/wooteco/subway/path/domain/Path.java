package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    final List<Station> stations;
    final int distance;

    public Path() {
        this.stations = Collections.emptyList();
        this.distance = 0;
    }

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return distance;
    }
}
