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

    public LineStation findLineStation(Long preStationId, Long stationId) {
        return lineStations.stream()
                .filter(lineStation -> lineStation.is(preStationId, stationId) || lineStation.is(stationId, preStationId))
                .findFirst()
                .orElse(LineStation.empty());
    }
}
