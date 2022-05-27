package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> stations;
    private final double distance;
    private final List<Long> lineIds;

    public Path(final List<Station> stations, final double distance, final List<Long> lineIds) {
        this.stations = stations;
        this.distance = distance;
        this.lineIds = lineIds;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public List<Long> getLineIds() {
        return lineIds;
    }
}
