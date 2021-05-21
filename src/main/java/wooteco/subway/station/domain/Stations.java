package wooteco.subway.station.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stations {
    private Map<Long, Station> stations;

    public Stations(List<Station> stationList) {
        if (stationList.isEmpty()) {
            throw new IllegalStateException("등록된 역이 없습니다.");
        }
        this.stations = init(stationList);
    }

    private Map<Long, Station> init(List<Station> stationList) {
        stations = new HashMap<>();
        for (Station station : stationList) {
            stations.put(station.getId(), station);
        }
        return stations;
    }

    public List<Station> findStationsOnPath(List<Long> stationIds) {
        return stationIds.stream()
                .map(id -> stations.get(id))
                .collect(Collectors.toList());
    }
}
