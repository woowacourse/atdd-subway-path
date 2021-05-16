package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
