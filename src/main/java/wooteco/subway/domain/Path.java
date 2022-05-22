package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final List<Long> lines;
    private final int distance;

    public Path(List<Station> stations, List<Long> lines, int distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Long> getLines() {
        return lines;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Path)) {
            return false;
        }
        Path path = (Path) o;
        return distance == path.distance && Objects.equals(stations, path.stations) && Objects.equals(
                lines, path.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, lines, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", lines=" + lines +
                ", distance=" + distance +
                '}';
    }
}
