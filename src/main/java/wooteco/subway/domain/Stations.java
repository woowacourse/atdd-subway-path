package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> sortedStationsById(List<Long> stationIds) {
        return stationIds.stream()
            .map(this::findStationById)
            .collect(Collectors.toList());
    }

    private Station findStationById(Long stationId) {
        return stations.stream()
            .filter(i -> i.matchId(stationId))
            .findAny()
            .orElseThrow(() ->new IllegalArgumentException("ID에 해당하는 Station이 존재하지 않습니다."));
    }
}
