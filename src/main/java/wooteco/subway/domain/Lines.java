package wooteco.subway.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> allStations() {
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .collect(Collectors.toSet());
    }

    public Set<Section> allSections() {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toSet());
    }
}
