package wooteco.subway.path.domin;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    final List<Station> stations;
    final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return this.stations;
    }
}
