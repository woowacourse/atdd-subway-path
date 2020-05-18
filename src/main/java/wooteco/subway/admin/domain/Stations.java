package wooteco.subway.admin.domain;

import wooteco.subway.admin.domain.exception.NoExistStationException;

import java.util.List;

public class Stations {
    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station findStation(Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(NoExistStationException::new);
    }
}

