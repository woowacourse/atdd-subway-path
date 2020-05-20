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

    public String findNameById(final Long id) {
        return stations.stream()
                .filter(station -> station.isSameId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 역 ID(%d)입니다.", id)))
                .getName();
    }

    public Long findIdByName(final String name) {
        return stations.stream()
                .filter(station -> station.isSameName(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 역 이름(%s)입니다.", name)))
                .getId();
    }
}
