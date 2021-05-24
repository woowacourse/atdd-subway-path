package wooteco.subway.line.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import wooteco.subway.station.domain.Station;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> toDistinctStations() {
        return lines.stream()
            .flatMap(Line::toStationStream)
            .collect(Collectors.toSet());
    }
}
