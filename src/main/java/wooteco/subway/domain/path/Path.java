package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> stations;
    private final List<Line> lines;
    private final int distance;

    public Path(List<Station> stations, List<Line> lines, int distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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

}
