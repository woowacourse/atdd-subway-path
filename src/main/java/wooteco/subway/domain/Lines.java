package wooteco.subway.domain;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public Stations stations() {
        final Map<Long, Station> stations = lines.stream()
            .flatMap(line -> line.getStations().stream())
            .collect(toMap(Station::getId, Function.identity()));
        return new Stations(stations);
    }

    public Set<Section> allSections() {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .collect(Collectors.toSet());
    }
}
