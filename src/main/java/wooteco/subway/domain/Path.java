package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final List<Line> usedLines;
    private final int distance;

    public Path(List<Station> stations, List<Line> usedLines, int distance) {
        this.stations = stations;
        this.usedLines = usedLines;
        this.distance = distance;
    }

    public static Path from(List<Section> sections, Station departure, Station arrival) {
        ShortestPath shortestPath = ShortestPath.generate(sections, departure, arrival);
        final List<Station> stations = shortestPath.getPath();
        final int distance = shortestPath.getDistance();
        final List<Line> lines = shortestPath.getUsedLines();

        return new Path(stations, lines, distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getMaxExtraFare() {
        return usedLines.stream()
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElseThrow();
    }
}
