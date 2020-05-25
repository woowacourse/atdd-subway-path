package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stations {
    private List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = stations;
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public static Stations empty() {
        return new Stations(new ArrayList<>());
    }

    public Map<Long, Station> convertMap() {
        return stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    public Stations getMatchingStationsByIds(List<Long> stationIds) {
        List<Station> matchingStations = stations.stream()
                .filter(station -> stationIds.contains(station.getId()))
                .collect(Collectors.toList());
        return Stations.of(matchingStations);
    }

    public List<Station> getStations() {
        return stations;
    }
}
