package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> stations;
    private final Long distance;
    private final List<Long> lineIds;

    public Path(List<Station> stations, Long distance, List<Long> lineIds) {
        this.stations = stations;
        this.distance = distance;
        this.lineIds = lineIds;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public List<Long> getLineIds() {
        return lineIds;
    }
}
