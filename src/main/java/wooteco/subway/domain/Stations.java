package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Station findStationById(Long stationId) {
        return stations.stream()
                .filter(station -> station.hasId(stationId))
                .findFirst()
                .orElseThrow();
    }

    public List<Station> arrangeStationsByIds(List<Long> stationIds) {
        return stationIds.stream()
                .map(this::findStationById)
                .collect(Collectors.toList());
    }
}
