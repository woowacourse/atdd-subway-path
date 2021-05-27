package wooteco.subway.path.domain;

import java.util.Collections;
import java.util.List;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> stations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
