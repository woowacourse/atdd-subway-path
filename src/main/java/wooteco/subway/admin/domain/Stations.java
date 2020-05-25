package wooteco.subway.admin.domain;

import wooteco.subway.admin.domain.exception.NoExistStationException;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Stations makePathStations(List<Long> shortestPath) {
        return shortestPath.stream()
                .map(this::findStation)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));
    }

    private Station findStation(Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(NoExistStationException::new);
    }

    public List<Station> getStations() {
        return stations;
    }
}

