package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import wooteco.subway.admin.exception.StationNotFoundException;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        validateStations(stations);
        this.stations = stations;
    }

    private void validateStations(List<Station> stations) {
        if (Objects.isNull(stations)) {
            throw new StationNotFoundException();
        }
    }

    public Station findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.isSameName(stationName))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(stationName));
    }

    public Map<Long, Station> generateStationMapper() {
        return stations.stream()
                .collect(Collectors.toMap(
                        Station::getId,
                        station -> station));
    }

    public List<Station> getStations() {
        return stations;
    }
}
