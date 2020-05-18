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

    public LineStation findLineStation(Long preStationId, Long stationId) {
        for (LineStation lineStation : lineStations) {
            if (lineStation.isSamePreStationIdAndStationId(preStationId, stationId)
                    || lineStation.isSamePreStationIdAndStationId(stationId, preStationId)) {
                return lineStation;
            }
        }
        return LineStation.empty();
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }
}
