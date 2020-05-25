package wooteco.subway.admin.domain;

import java.util.List;

public class LineStations {
    private List<LineStation> lineStations;

    public LineStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public int getInformation(List<Long> shortestPath, PathType type) {
        return lineStations.stream()
                .filter(lineStation -> shortestPath.contains(lineStation.getStationId()))
                .filter(lineStation -> shortestPath.contains(lineStation.getPreStationId()))
                .mapToInt(type::getGetInformation)
                .sum();
    }
}
