package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LineStations {
    private final List<LineStation> lineStations;

    public LineStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public Set<Long> getAllLineStationId() {
        return lineStations.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toSet());
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    // todo : 이거 리팩토링
    public LineStation findLineStation(Long preStationId, Long stationId) {
        for (LineStation lineStation : lineStations) {
            if (lineStation.is(preStationId, stationId)) {
                return lineStation;
            }
            if (lineStation.is(stationId, preStationId)) {
                return lineStation;
            }
        }
        return LineStation.empty();
    }
}
