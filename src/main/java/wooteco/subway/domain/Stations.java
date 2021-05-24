package wooteco.subway.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Stations {

    private Map<Long, Station> stations;

    public Stations(Map<Long, Station> stations) {
        this.stations = stations;
    }

    public Set<Long> stationIds() {
        return stations.keySet();
    }

    public Station findStationById(Long stationId) {
        return stations.get(stationId);
    }
}
