package wooteco.subway.admin.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    public Stations(final List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> findAllById(final List<Long> ids) {
        return stations.stream()
                .filter(station -> ids.contains(station.getId()))
                .collect(Collectors.toList());
    }
}
