package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {

    private List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Station getStationById(long id) {
        return stations.stream()
                .filter(station -> station.isSameId(id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id를 찾을 수 없습니다."));
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }
}
