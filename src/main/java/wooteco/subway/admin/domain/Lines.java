package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import wooteco.subway.admin.exception.LineNotFoundException;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        validateLines(lines);
        this.lines = lines;
    }

    private void validateLines(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new LineNotFoundException();
        }
    }

    public List<LineStation> getLineStations() {
        return lines.stream()
                .flatMap(line -> line.getLineStations().stream())
                .filter(LineStation::hasPreStation)
                .collect(Collectors.toList());
    }

    public List<Line> getLines() {
        return lines;
    }
}
