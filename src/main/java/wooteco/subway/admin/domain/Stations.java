package wooteco.subway.admin.domain;

import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Station findStationById(Long id) {
        return stations.stream()
                .filter(station -> station.is(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Station findStationByName(String name) {
        return stations.stream()
                .filter(station -> station.is(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
