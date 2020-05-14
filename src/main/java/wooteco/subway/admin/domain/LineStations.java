package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LineStations {
    private final List<LineStation> lineStations;

    public LineStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    private List<LineStation> getAllLineStation(List<Line> allLines) {
        return allLines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
