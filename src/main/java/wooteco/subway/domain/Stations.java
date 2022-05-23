package wooteco.subway.domain;

import java.util.List;
import java.util.NoSuchElementException;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> get() {
        return stations;
    }

    public Station find(Long id) {
        return stations.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 역 아이디입니다."));
    }
}
