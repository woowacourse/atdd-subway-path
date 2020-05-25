package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public LineStations makeLineStation() {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(lineStation -> lineStation.getPreStationId() != null)
                .collect(Collectors.collectingAndThen(Collectors.toList(), LineStations::new));
    }

    public List<Line> getLines() {
        return lines;
    }
}
