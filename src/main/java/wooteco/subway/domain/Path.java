package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
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
        return distance == path.distance && stations.equals(path.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }

}
