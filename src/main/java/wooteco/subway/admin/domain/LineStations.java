package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LineStations {
    private final List<LineStation> lineStations;

    public LineStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public LineStations() {
        this(new ArrayList<>());
    }

    public void add(LineStation lineStation) {
        lineStations.add(lineStation);
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

    public int computeTotalDuration() {
        return lineStations.stream()
                .mapToInt(LineStation::getDuration)
                .sum();
    }

    public int computeTotalDistance() {
        return lineStations.stream()
                .mapToInt(LineStation::getDistance)
                .sum();
    }


    public Set<Long> getAllLineStationId() {
        return lineStations.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toSet());
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }
}
