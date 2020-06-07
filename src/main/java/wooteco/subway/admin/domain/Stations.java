package wooteco.subway.admin.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Station> findStationPath(List<Long> path) {
        return path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList());
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(it -> it.getId().equals(stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
