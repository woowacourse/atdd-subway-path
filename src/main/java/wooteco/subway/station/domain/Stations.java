package wooteco.subway.station.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Stations {

    private final Map<Long, Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    public Station getStationById(long id) {
        return stations.get(id);
    }
}
