package wooteco.subway.domain;

import java.util.List;
import java.util.Objects;

public class Path {

    private final List<Station> stations;
    private final List<Integer> extraFares;
    private final int distance;

    public Path(List<Station> stations, List<Integer> extraFares, int distance) {
        this.stations = stations;
        this.extraFares = extraFares;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Integer> getExtraFares() {
        return extraFares;
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
        return getDistance() == path.getDistance() && Objects.equals(getStations(),
            path.getStations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStations(), getDistance());
    }
}
