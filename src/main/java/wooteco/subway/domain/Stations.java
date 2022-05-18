package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> sortedStationsById(List<Long> stationIds) {
        List<Station> sortedStations = new ArrayList<>();
        for (Long stationId : stationIds) {
            sortedStations.add(findStation(stationId, stations));
        }
        return sortedStations;
    }

    private Station findStation(Long stationId, List<Station> stations) {
        return stations.stream()
            .filter(i -> i.getId().equals(stationId))
            .findAny()
            .orElseThrow(() ->new IllegalArgumentException("ID에 해당하는 Station이 존재하지 않습니다."));
    }
}
