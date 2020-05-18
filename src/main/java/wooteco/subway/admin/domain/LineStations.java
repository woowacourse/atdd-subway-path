package wooteco.subway.admin.domain;

import wooteco.subway.admin.exception.WrongPathException;

import java.util.ArrayList;
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

    public LineStations findLineStationsByIds(List<Long> ids) {
        List<LineStation> findLineStations = new ArrayList<>();

        for (int i = 0; i < ids.size() - 1; i++) {
            Long preStationId = ids.get(i);
            Long stationId = ids.get(i + 1);

            findLineStations.add(findLineStationById(preStationId, stationId));
        }
        return new LineStations(findLineStations);
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
