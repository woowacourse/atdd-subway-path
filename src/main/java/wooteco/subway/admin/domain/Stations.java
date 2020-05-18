package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.NoSuchStationException;

import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Station findStationById(Long id) {
        return stations.stream()
                .filter(station -> station.isSameId(id))
                .findFirst()
                .orElseThrow(NoSuchStationException::new);
    }

    public Station findStationByName(String name) {
        return stations.stream()
                .filter(station -> station.isSameId(name))
                .findFirst()
                .orElseThrow(NoSuchStationException::new);
    }
}
