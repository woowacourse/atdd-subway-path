package wooteco.subway.domain.station;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.exception.DataNotExistException;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public boolean contains(Long id) {
        return getStationIds().contains(id);
    }

    public Station findStationById(Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("지하철역을 찾을 수 없습니다."));
    }

    public Stations filter(List<Long> stationIds) {
        return new Stations(stationIds.stream()
                .map(this::findStationById)
                .collect(Collectors.toList()));
    }

    public List<Long> getStationIds() {
        return stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return stations;
    }
}
