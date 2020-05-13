package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public List<LineStation> getEdges() {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
