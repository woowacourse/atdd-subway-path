package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.WrongPathException;

import java.util.List;

public class LineStations {
    private final List<LineStation> lineStations;

    public LineStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public LineStation findLineStationById(Long preStationId, Long stationId) {
        return lineStations
                .stream()
                .filter(lineStation -> lineStation.isLineStationOf(preStationId, stationId))
                .findFirst()
                .orElseThrow(WrongPathException::new);
    }

    public int getDistance() {
        return lineStations
                .stream()
                .mapToInt(LineStation::getDistance)
                .sum();
    }

    public int getDuration() {
        return lineStations
                .stream()
                .mapToInt(LineStation::getDuration)
                .sum();
    }
}
