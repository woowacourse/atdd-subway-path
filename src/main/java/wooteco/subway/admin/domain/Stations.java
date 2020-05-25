package wooteco.subway.admin.domain;

import wooteco.subway.admin.domain.exception.NoSuchStationException;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public void add(Station station) {
        stations.add(station);
    }

    public Stations listOf(List<Long> ids) {
        List<Station> stationsByIds = new ArrayList<>();

        for (Long id : ids) {
            stationsByIds.add(findStationById(id));
        }

        return new Stations(stationsByIds);
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

    public List<Station> getStations() {
        return stations;
    }
}
