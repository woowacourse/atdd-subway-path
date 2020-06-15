package wooteco.subway.admin.domain;

import java.util.ArrayList;
import java.util.List;

public class LineStations {
    private List<LineStation> lineStations = new ArrayList<>();

    public LineStations() {
    }

    public int calculateFastestDuration(List<Long> path) {
        return extractPathLineStation(path).stream()
                .mapToInt(LineStation::getDuration)
                .sum();
    }

    public int calculateShortestDistance(List<Long> path) {
        return extractPathLineStation(path).stream()
                .mapToInt(LineStation::getDistance)
                .sum();
    }

    private List<LineStation> extractPathLineStation(List<Long> path) {
        List<LineStation> paths = new ArrayList<>();

        for (int i = 1; i < path.size(); i++) {
            Long stationId = path.get(i);
            Long finalPreStationId = path.get(i - 1);
            LineStation lineStation = calculateLineStation(lineStations, stationId, finalPreStationId);

            paths.add(lineStation);
        }

        return paths;
    }

    private LineStation calculateLineStation(List<LineStation> lineStations, Long stationId, Long finalPreStationId) {
        return lineStations.stream()
                .filter(it -> it.isLineStationOf(finalPreStationId, stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public List<LineStation> findLineStationsWithOutSourceLineStation(Lines lines) {
        lines.addLineStationsWithOutSourceLineStation(lineStations);
        return lineStations;
    }
}
