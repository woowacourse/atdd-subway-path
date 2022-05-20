package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final Integer distance;

    public Path(List<Station> stations, Integer distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
