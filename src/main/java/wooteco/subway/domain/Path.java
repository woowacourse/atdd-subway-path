package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;

public class Path {

    private final List<Station> stations;
    private final Lines lines;
    private final int distance;

    public Path(final List<Station> stations, final List<Line> lines, final int distance) {
        this.stations = stations;
        this.lines = new Lines(lines);
        this.distance = distance;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Lines getLines() {
        return lines;
    }

    public int getDistance() {
        return distance;
    }
}
