package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.StationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Long findIdByName(String name) {
        return stations
                .stream()
                .filter(station -> station.getName().equals(name))
                .map(Station::getId)
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public Station findStationById(Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public List<Station> filterStationsByIds(List<Long> ids) {
        return stations.stream()
                .filter(station -> ids.contains(station.getId()))
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return stations;
    }

    public boolean isNotContains(Long stationId) {
        return stations.stream()
                .noneMatch(station -> station.getId().equals(stationId));
    }
}
