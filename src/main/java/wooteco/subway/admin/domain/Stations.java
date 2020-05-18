package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.StationNotFoundException;

import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long findStationIdByName(String name) {
        return stations
                .stream()
                .filter(station -> station.getName().equals(name))
                .map(Station::getId)
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public Station findStationById(Long id) {
        return stations
                .stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }
}
