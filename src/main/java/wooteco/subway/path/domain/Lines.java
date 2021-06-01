package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> allStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(toSet());
    }

    public Set<Section> allSections() {
        return lines.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .collect(toSet());
    }
}
