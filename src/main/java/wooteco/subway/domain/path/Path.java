package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.Station;

public class Path {
    private final List<Station> stations;
    private final List<Long> lineIds;
    private final double distance;

    public Path(List<Station> stations, List<Long> lineIds, double distance) {
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

    public double getDistance() {
        return distance;
    }
}
