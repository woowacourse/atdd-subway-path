package wooteco.subway.domain.line;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wooteco.subway.domain.Station;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Set<Station> extractStations() {
        Set<Station> stations = new HashSet<>();
        for (Line line : lines) {
            stations.addAll(line.getStations());
        }
        return stations;
    }

    public List<Line> getLines() {
        return lines;
    }
}
