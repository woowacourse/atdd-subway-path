package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<LineStation> getLineStations() {
        return lines
                .stream()
                .flatMap(line -> line.getStations()
                        .stream()
                        .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId())))
                .collect(Collectors.toList());
    }

    public List<Long> getLineStationsId() {
        return lines
                .stream()
                .flatMap(it -> it.getLineStationsId().stream())
                .collect(Collectors.toList());
    }
}
