package wooteco.subway.station.domain;

import java.util.ArrayList;
import java.util.List;

public class Stations {
    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> sortedStation(List<Long> stationIds) {
        List<Station> sortedStation = new ArrayList<>();
        for (Long stationId : stationIds) {
            Station findStation = stations.stream()
                    .filter(station -> station.isSameId(stationId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
            sortedStation.add(findStation);
        }
        return sortedStation;
    }
}
