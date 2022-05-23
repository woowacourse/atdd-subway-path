package wooteco.subway.domain;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final List<Long> lineIds;
    private final int distance;

    public Path(List<Station> stations, List<Long> lineIds, int distance) {
        this.stations = stations;
        this.lineIds = lineIds;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Long> getLineIds() {
        return lineIds;
    }

    public int getDistance() {
        return distance;
    }
}
