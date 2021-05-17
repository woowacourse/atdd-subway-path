package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wooteco.exception.notFound.StationNotFoundException;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = new ArrayList<>(stations);
    }

    public List<Station> toList() {
        return Collections.unmodifiableList(stations);
    }

    public Station findStationById(Long id) {
        return stations.stream()
            .filter(station -> station.getId().equals(id)).findAny()
            .orElseThrow(StationNotFoundException::new);
    }
}
